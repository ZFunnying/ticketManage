package com.cmit.kapok.system.entity.sys_depart;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalDateTime;
    import java.io.Serializable;
    import javax.validation.constraints.Size;
    import javax.validation.constraints.NotNull;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 组织机构表
    * </p>
*
* @author 
* @since 2022-03-08
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class SysDepart implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * ID
            */
            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

            /**
            * 父机构ID
            */
    private Integer parentId;

            /**
            * 机构/部门名称
            */
                    @Size(max =  100,message = "机构/部门名称超过最大长度[100]")
                    @NotNull(message = "机构/部门名称不能为空")
    private String departName;

            /**
            * 英文名
            */
                    @Size(max =  500,message = "英文名超过最大长度[500]")
    private String departNameEn;

            /**
            * 缩写
            */
                    @Size(max =  500,message = "缩写超过最大长度[500]")
    private String departNameAbbr;

            /**
            * 排序
            */
    private Integer departOrder;

            /**
            * 描述
            */
                    @Size(max =  500,message = "描述超过最大长度[500]")
    private String description;

            /**
            * 机构类别 1组织机构，2岗位
            */
                    @Size(max =  10,message = "机构类别 1组织机构，2岗位超过最大长度[10]")
                    @NotNull(message = "机构类别 1组织机构，2岗位不能为空")
    private String orgCategory;

            /**
            * 机构类型 1一级部门 2子部门
            */
                    @Size(max =  10,message = "机构类型 1一级部门 2子部门超过最大长度[10]")
    private String orgType;

            /**
            * 机构编码
            */
                    @Size(max =  64,message = "机构编码超过最大长度[64]")
                    @NotNull(message = "机构编码不能为空")
    private String orgCode;

            /**
            * 手机号
            */
                    @Size(max =  32,message = "手机号超过最大长度[32]")
    private String mobile;

            /**
            * 传真
            */
                    @Size(max =  32,message = "传真超过最大长度[32]")
    private String fax;

            /**
            * 地址
            */
                    @Size(max =  100,message = "地址超过最大长度[100]")
    private String address;

            /**
            * 备注
            */
                    @Size(max =  500,message = "备注超过最大长度[500]")
    private String memo;

            /**
            * 状态（1启用，0不启用）
            */
    private Boolean status;

            /**
            * 删除状态（0，正常，1已删除）
            */
    private Boolean delFlag;

            /**
            * 创建人
            */
                    @Size(max =  32,message = "创建人超过最大长度[32]")
    private String createBy;

            /**
            * 创建日期
            */
    private LocalDateTime createTime;

            /**
            * 更新人
            */
                    @Size(max =  32,message = "更新人超过最大长度[32]")
    private String updateBy;

            /**
            * 更新日期
            */
    private LocalDateTime updateTime;


}
