package com.touch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touch.entity.User;
import com.touch.mapper.UserMapper;
import com.touch.service.UserService;
import com.touch.utils.DistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    String lockKey = "myLock";
    String requestId = "request1";
    int expireTime = 10000; // 10秒

    @Autowired
    DistributedLock distributedLock;
    @Autowired
    RedisTemplate redisTemplate;

    public void add() {
        while (true) {
            boolean isLocked = distributedLock.tryGetDistributedLock(lockKey, requestId, expireTime);
            if (isLocked) {
                try {
                    Long age = baseMapper.selectCount(new LambdaQueryWrapper<User>());
                    User user = new User();
                    user.setPassword(age.toString());
                    user.setName("22");
                    user.setCreateTime(new Date());
                    user.setId(IdWorker.getId());
                    List<User> list =new ArrayList<>();
                    for(int i =0;i<10000000;i++){
                        User user1 = new User();
                        user1.setName("22");
                        user1.setCreateTime(new Date());
                        user1.setId(IdWorker.getId());
                        list.add(user1);
                        //baseMapper.insert(user);
                    }
                    this.saveBatch(list);
                    break;
                }  finally {
                    boolean isReleased = distributedLock.releaseDistributedLock(lockKey, requestId);
                    if (isReleased) {
                        System.out.println("Lock released.");
                    } else {
                        System.out.println("Failed to release lock.");
                    }
                }
            } else {
                System.out.println("Failed to acquire lock.");
            }

        }
    }

    @Override
    public void redisTest1() {

    }
}
