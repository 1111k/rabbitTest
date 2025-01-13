package com.touch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touch.entity.User;

public interface UserService extends IService<User> {
    void add();

    void redisTest1();
}
