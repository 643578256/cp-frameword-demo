package com.winshare.demo.proxyspr;

import com.winshare.demo.rest.Person;



@MyService
public interface TestA2 {
    String insert(Person userVo);

    void updateUser(Person userVo);
}
