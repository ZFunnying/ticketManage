package com.cmit.kapok.system.entity.sys_depart;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.io.Serializable;
    import javax.validation.constraints.Size;
    import javax.validation.constraints.NotNull;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author 
* @since 2022-03-08
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class SysUserDepart implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * id
            */
            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

            /**
            * 用户id
            */
    private Integer userId;

            /**
            * 部门id
            */
    private Integer depId;


}
