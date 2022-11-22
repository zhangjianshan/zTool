package com.ztool.demo.mybatis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ztool.mybatis.model.BaseModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author zhangjianshan on 2022-11-22
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("sys_user")
public class SysUserModel extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1329854007607117948L;

    @TableId(type = IdType.ASSIGN_ID)
    Long userId;

    @TableField("username")
    String username;

    @TableField("password")
    String password;

    @TableField("is_admin")
    Integer isAdmin;

    @TableField("real_name")
    String realName;

    @TableField("create_by")
    Long createBy;

    @TableField("user_status")
    Boolean userStatus;


}

