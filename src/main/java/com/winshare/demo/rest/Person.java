package com.winshare.demo.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class Person implements Serializable {
    private String name;
    private Integer age;
}
