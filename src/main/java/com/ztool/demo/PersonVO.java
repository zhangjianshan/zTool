package com.ztool.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonVO {
    //姓名
    private String name;

    //年龄
    private Integer age;
}
