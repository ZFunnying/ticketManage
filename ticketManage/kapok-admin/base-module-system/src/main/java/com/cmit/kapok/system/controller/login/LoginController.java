package com.cmit.kapok.system.controller.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.constants.UserConstants;
import com.cmit.kapok.system.api.user.SysUserService;
import com.cmit.kapok.system.controller.login.vo.LoginParams;
import com.cmit.kapok.system.controller.login.vo.PasswordParams;
import com.cmit.kapok.system.controller.login.vo.SsoLoginParam;
import com.cmit.kapok.system.entity.user.SysUser;
import com.cmit.kapok.system.entity.user.TokenUserDAO;
import com.cmit.kapok.system.entity.user.User;
import com.cmit.kapok.system.utils.PasswordUtil;
import com.cmit.kapok.system.utils.SHA1Util;
import com.cmit.kapok.system.utils.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cmit.kapok.constants.Constants.TOKEN_EXPIRE_TIME;
import static com.cmit.kapok.constants.RedisPrefixConstants.SYSTEM_USER_TOKEN_PREFIX;
import static com.cmit.kapok.constants.UserConstants.*;

@Controller
@RequestMapping("/user")
@Api(tags = "登录相关")
public class LoginController {
  private final Logger logger = LoggerFactory.getLogger(LoginController.class);
  @Resource
  private SysUserService userService;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;
  @Autowired
  private TokenUtil tokenUtil;
  @Autowired
  private ObjectMapper objectMapper;
  @Value("${chudayi.host}")
  private String chudayiHost;
  @Value("${chudayi.appKey}")
  private String chudayiAppKey;
  @Value("${chudayi.appSecret}")
  private String chudayiAppSecret;
  @Autowired
  private RestTemplate restTemplate;

  @ApiOperation(value="修改密码", notes="")
  @ResponseBody
  @PostMapping("/changePassword")
  public Result changePassword(@RequestBody PasswordParams passwordParams, HttpServletRequest request, Map<String, Object> map) {
    int id = Integer.parseInt( passwordParams.getId());
    String oldPassword = passwordParams.getOriginPassword();
    String password = passwordParams.getPassword();
    SysUser getOldSalt = userService.getById(id);
    Object salt = PasswordUtil.getRandomString(8);//盐值 取一个随机字符
    if(userService.checkOldPassword(id,PasswordUtil.encryptionCode(oldPassword,getOldSalt.getSalt())) < 1){
      return ResultGenerator.genFailResult("原密码错误");
    }else {
      SysUser sysUser = new SysUser();
      sysUser.setId(id);
      sysUser.setPassword(PasswordUtil.encryptionCode(password,salt));
      sysUser.setSalt((String) salt);
      userService.updateById(sysUser);
    }
    return ResultGenerator.genSuccessResult();
  }

  @ResponseBody
  @RequestMapping(value = "/sso/login", method = RequestMethod.POST)
  public Result SsoLogin(@RequestBody SsoLoginParam ssoLoginParam) throws JsonProcessingException {
    Map<String, String> params = new HashMap<>();
    params.put("token", ssoLoginParam.getToken());
    StringBuilder sb = new StringBuilder();
    String timeSpan = String.valueOf(System.currentTimeMillis());
    String nonce = PasswordUtil.getRandomString(8);
    String sign = getSign(timeSpan, nonce);
    sb.append(this.chudayiHost).append("/cdy/api/getUserInfo").append("?appkey=").append(this.chudayiAppKey)
            .append("&timespan=").append(timeSpan).append("&nonce=").append(nonce).append("&signature=").append(sign);
    HttpEntity<Map> requestEntity = new HttpEntity<>(params);
    logger.info("请求地址：" + sb.toString());
    logger.info("请求appKey：" + this.chudayiAppKey);

    String res = "";
    try {
      res = restTemplate.postForObject(sb.toString(), requestEntity, String.class);
    } catch (Exception e) {
      logger.error("sso登录接口异常：", e);
      return ResultGenerator.genFailResult("sso登录接口异常：" + e.getMessage());
    }

    logger.info("[/auth/getUserInfo] response: {}", res);
    JsonNode jsonNode = objectMapper.readTree(res);
    if (!jsonNode.has("code") || !"2000".equals(jsonNode.get("code").asText())) {
      return ResultGenerator.genFailResult("sso登录接口错误：" + jsonNode.get("message").asText());
    }

    if (!jsonNode.has("data")) {
      return ResultGenerator.genFailResult("返回信息为空");
    }
    if (!jsonNode.get("data").has("userId")
            || !jsonNode.get("data").has("userName")
            || !jsonNode.get("data").has("realName")) {
      return ResultGenerator.genFailResult("用户信息不完整");
    }

    String id = jsonNode.get("data").get("userId").asText();
    String userName = jsonNode.get("data").get("userName").asText();
    String realName = jsonNode.get("data").get("realName").asText();
    String phone = jsonNode.get("data").get("phone").asText();
    String email = jsonNode.get("data").get("email").asText();
    List roles = objectMapper.convertValue(jsonNode.get("data").get("roles"), new TypeReference<ArrayList>() {});

    Map<String, Object> reMap = new HashMap<String, Object>();
    Map userInfo = new HashMap();
    userInfo.put("userId", id);
    userInfo.put("userName", userName);
    userInfo.put("realName", realName);
    userInfo.put("phone", phone);
    userInfo.put("email", email);
    userInfo.put("roles", roles);
    userInfo.put("userType",SYSTEM_USER_TYPE_SSO);
    String jwt = tokenUtil.createJWT(id, userName, realName, 1000 * (TOKEN_EXPIRE_TIME));
    stringRedisTemplate.opsForValue().set(SYSTEM_USER_TOKEN_PREFIX + jwt, objectMapper.writeValueAsString(userInfo), 2, TimeUnit.DAYS); // token请求过期时间
    reMap.put("token", jwt);
    reMap.put("message", "登录成功");
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    logger.warn(String.format("当前SSO登录用户：%s，登录时间：%s", userName, fmt.format(new Date())));
    return ResultGenerator.genSuccessResult(reMap);
  }

  @ResponseBody
  @PostMapping("/login")
  public Result login(@RequestBody LoginParams loginParams, HttpServletRequest request, Map<String, Object> map) throws JsonProcessingException {
    String username = loginParams.getUsername().trim();
    String password = loginParams.getPassword();
    String kaptcha = loginParams.getKaptcha().trim();

    String sessionKaptcha = (String)request.getSession().getAttribute("vrifyCode");
    request.getSession().removeAttribute("vrifyCode");
    if (kaptcha == null || sessionKaptcha == null || !kaptcha.toLowerCase().equals(sessionKaptcha.toLowerCase())) {
      logger.error("验证码错误");
      return ResultGenerator.genFailResult("验证码错误");
    }
    SysUser loginUser = userService.getOne(new QueryWrapper<SysUser>().eq("user_name",username));
    if(null == loginUser){
      logger.error("用户不存在:{}",username);
      return ResultGenerator.genFailResult("用户名或密码错误");
    }
    if(!loginUser.getPassword().equals(PasswordUtil.encryptionCode(password,loginUser.getSalt()))){
      logger.error("输入密码错误:{}",username);
      return ResultGenerator.genFailResult("用户名或密码错误");
    }
    Map<String, Object> reMap = new HashMap<String, Object>();
    String jwt = tokenUtil.createJWT(loginUser.getId().toString(), loginUser.getUserName()
            , loginUser.getRealName(),1000*(TOKEN_EXPIRE_TIME));

    Map userInfo = new HashMap();
    userInfo.put("userId",loginUser.getId().toString());
    userInfo.put("userName",loginUser.getUserName());
    userInfo.put("realName",loginUser.getRealName());
    userInfo.put("userType",SYSTEM_USER_TYPE_ADMIN);
    stringRedisTemplate.opsForValue().set(SYSTEM_USER_TOKEN_PREFIX+jwt,objectMapper.writeValueAsString(userInfo),2, TimeUnit.DAYS); // token请求过期时间
    reMap.put("token", jwt);
    reMap.put("message", "登录成功");
    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    logger.warn(String.format("当前登录用户：%s，登录时间：%s",loginUser.getUserName(),fmt.format(new Date())));
    return ResultGenerator.genSuccessResult(reMap);
  }
  @ApiOperation(value="获取用户信息", notes="")
  @ResponseBody
  @GetMapping("/info")
  public Result getUserInfo(HttpServletRequest request) {
    TokenUserDAO tokenUserDAO = tokenUtil.getTokenUserInfo(request);
    if(SYSTEM_USER_TYPE_SSO.equals(tokenUserDAO.getUserType())){
      Map<String, Object> reMap = new HashMap<String, Object>();
      reMap.put("name", tokenUserDAO.getUserName());
      reMap.put("id", tokenUserDAO.getUserId());
      reMap.put("realName", tokenUserDAO.getRealName());
      reMap.put("phone", tokenUserDAO.getPhone());
      reMap.put("email", tokenUserDAO.getEmail());
      reMap.put("roles", tokenUserDAO.getRoles());
      return ResultGenerator.genSuccessResult(reMap);
    }else {
      String id = tokenUserDAO.getUserId();
      User result = userService.queryById(Integer.parseInt(id));
      Map<String, Object> reMap = new HashMap<String, Object>();
      reMap.put("token", result.getToken());
      reMap.put("name", result.getUserName());
      reMap.put("id",result.getId());
      reMap.put("realName",result.getRealName());
      if(null != result.getAvatar()){
        reMap.put("avatar", result.getAvatar());
      }
      else{
        reMap.put("avatar", "https://wpimg.wallstcn.com/007ef517-bafd-4066-aae4-6883632d9646");
      }
      //获取角色信息
      List roles = new ArrayList<String>();
      List rolesId = new ArrayList<String>();
      //如果有多条角色信息，分开添加
      String rolesStr = result.getRole();
      //无角色信息
      if("".equals(rolesStr) || null == rolesStr){
        return ResultGenerator.genFailResult("该用户还没有分配角色");
      }
      if(rolesStr.contains(",")){
        roles = Arrays.asList(rolesStr.split(","));
        rolesId = Arrays.asList(result.getRoleId().split(","));
      }
      else {
        roles.add(rolesStr);
        rolesId.add(result.getRoleId());
      }
      reMap.put("roles", roles);
      reMap.put("rolesId", rolesId);
      reMap.put("introduction", result.getIntroduction());
      return ResultGenerator.genSuccessResult(reMap);
    }

  }

  @ResponseBody
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public Result register(@RequestBody LoginParams loginParams, HttpServletRequest request) {
    String email = loginParams.getEmail().trim();
    String password = loginParams.getPassword();
    String userName = loginParams.getUsername().trim();
    String realName = loginParams.getRealName().trim();
    String introduction = loginParams.getIntroduction();
    Object salt = PasswordUtil.getRandomString(8);//盐值
    if (userService.getOne(new QueryWrapper<SysUser>().eq("user_name", userName)) != null) {
      return ResultGenerator.genFailResult("该用户名或邮箱已注册！");
    } else if (userService.getOne(new QueryWrapper<SysUser>().eq("email", email)) != null) {
      return ResultGenerator.genFailResult("该用户名或邮箱已注册！");
    } else {
      SysUser user = new SysUser();
      user.setUserName(userName);
      user.setEmail(email);
      user.setRealName(realName);
      user.setPassword(PasswordUtil.encryptionCode(password,salt));
      user.setSalt((String) salt);
      user.setAddTime(new Date());
      user.setIntroduction(introduction);
      userService.save(user);
      return ResultGenerator.genSuccessResult(user);
    }
  }
  @ApiOperation(value="退出登录，shiro退出，清空token", notes="")
  @ResponseBody
  @RequestMapping(value = "/userLogout", method = RequestMethod.POST)
  public Result logout(HttpServletRequest request){
    tokenUtil.destroyToken(request);
    return ResultGenerator.genSuccessResult();
  }

  private String getSign(String timespan, String nonce) {
    String verifySignature = SHA1Util.getSignature(this.chudayiAppSecret, nonce, timespan);
    return verifySignature;
  }

}
