package com.ztool.demo.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangjianshan1992
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonVO {
    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;
}
