package com.cmit.kapok.system.utils;

import com.cmit.kapok.system.entity.user.TokenUserDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import static com.cmit.kapok.constants.RedisPrefixConstants.SYSTEM_USER_TOKEN_PREFIX;
import static com.cmit.kapok.constants.RedisPrefixConstants.TOKEN_REFLASH_PREFIX;

/**
 *  * Toten令牌验证
 *  * @since: 2018年6月21日下午4:18:21 
 *  * @version: 1.0
 *  
 */
@Component
public class TokenUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	@Autowired
    ObjectMapper objectMapper;
    @Value("${jwt.token.secret}")
    private String tokenSecret;
  /**
   * 加密
   *
   * @param id
   * @param userName  userName
   * @param realName realName
   * @param ttlMillis
   * @return
   */
  public String createJWT(String id, String userName, String realName, long ttlMillis) {

    //The JWT signature algorithm we will be using to sign the token
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    //We will sign our JWT with our ApiKey secret
    byte[] apiKeySecretBytes = Base64.decodeBase64(tokenSecret);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    //Let's set the JWT Claims
    JwtBuilder builder = Jwts.builder().setId(id)
      .setIssuedAt(now)
      .claim("userName",userName)
      .claim("realName",realName)
      .signWith(signatureAlgorithm, signingKey);

    //if it has been specified, let's add the expiration
    if (ttlMillis >= 0) {
      long expMillis = nowMillis + ttlMillis;
      Date exp = new Date(expMillis);
      builder.setExpiration(exp);
    }

    //Builds the JWT and serializes it to a compact, URL-safe string
    return builder.compact();
  }

  /**
   * 解密
   *
   * @param jwt
   */
  public Claims parseJWT(String jwt) {
    //This line will throw an exception if it is not a signed JWS (as expected)
    Claims claims = Jwts.parser().setSigningKey(Base64.decodeBase64(tokenSecret))
      .parseClaimsJws(jwt).getBody();
//      logger.warn(String.format("ID：%s,Subject: %s,Issuer: %s,Expiration: %s",claims.getId()
//              ,claims.getSubject(),claims.getIssuer(),claims.getExpiration()));
//    System.out.println("ID: " + claims.getId());
//    System.out.println("Subject: " + claims.getSubject());
//    System.out.println("Issuer: " + claims.getIssuer());
//    System.out.println("Expiration: " + claims.getExpiration());
    return claims;
  }
  // 获取用户信息请勿调用该函数
  public TokenUserDAO verifyToken(String jwt){
    Claims claims = this.parseJWT(jwt);
    TokenUserDAO tokenUserDAO = new TokenUserDAO();
    tokenUserDAO.setUserId(claims.getId());
    tokenUserDAO.setUserName(String.valueOf(claims.get("userName")));
    tokenUserDAO.setRealName(String.valueOf(claims.get("realName")));
    tokenUserDAO.setUserType(String.valueOf(claims.get("userType")));
    return tokenUserDAO;
  }

  public void destroyToken(HttpServletRequest request){
    if (stringRedisTemplate == null) {
      stringRedisTemplate = SpringBeanFactoryUtils.getBean(StringRedisTemplate.class);
    }
    String token = request.getHeader("Authorization");
    stringRedisTemplate.delete(SYSTEM_USER_TOKEN_PREFIX+token);
    if(stringRedisTemplate.hasKey(TOKEN_REFLASH_PREFIX+token)){
      stringRedisTemplate.delete(stringRedisTemplate.opsForValue().get(TOKEN_REFLASH_PREFIX+token));
      stringRedisTemplate.delete(TOKEN_REFLASH_PREFIX+token);
    }
  }

  public TokenUserDAO getTokenUserInfo(HttpServletRequest request){
    if (stringRedisTemplate == null) {
      stringRedisTemplate = SpringBeanFactoryUtils.getBean(StringRedisTemplate.class);
    }
    if (objectMapper == null) {
      objectMapper = SpringBeanFactoryUtils.getBean(ObjectMapper.class);
    }
    String token = request.getHeader("Authorization");
    String userInfoString = "";
    if(stringRedisTemplate.hasKey(SYSTEM_USER_TOKEN_PREFIX.concat(token))){
      userInfoString = stringRedisTemplate.opsForValue().get(SYSTEM_USER_TOKEN_PREFIX.concat(token));
    } else if(stringRedisTemplate.hasKey(TOKEN_REFLASH_PREFIX.concat(token))){
      userInfoString = stringRedisTemplate.opsForValue().get(TOKEN_REFLASH_PREFIX.concat(token));
    } else {
      throw new RuntimeException("redis has no key.");
    }
    JsonNode userInfo = null;
    try {
      userInfo = objectMapper.readTree(userInfoString);
      String id = userInfo.get("userId").asText();
      String userName = userInfo.get("userName").asText();
      String realName = userInfo.get("realName").asText();
      String userType = userInfo.get("userType").asText();
      TokenUserDAO tokenUserDAO = new TokenUserDAO();
      tokenUserDAO.setUserId(id);
      tokenUserDAO.setUserName(userName);
      tokenUserDAO.setRealName(realName);
      tokenUserDAO.setUserType(userType);
      if(userInfo.has("email")){
        String email = userInfo.get("email").asText();
        tokenUserDAO.setEmail(email);
      }
      if(userInfo.has("phone")){
        String phone = userInfo.get("phone").asText();
        tokenUserDAO.setPhone(phone);
      }
      if(userInfo.has("roles")){
        List<String> roles = objectMapper.convertValue(userInfo.get("roles"), new TypeReference<ArrayList<String>>() {});
        tokenUserDAO.setRoles(roles);
      }

      return tokenUserDAO;
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Get userinfo error.",e);
    }
  }

}
