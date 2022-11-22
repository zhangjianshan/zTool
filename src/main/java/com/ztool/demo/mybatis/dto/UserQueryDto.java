package com.ztool.demo.mybatis.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjianshan
 */
@Data
public class UserQueryDto implements Serializable {

    private String userName;

    private String userStatus;

}
