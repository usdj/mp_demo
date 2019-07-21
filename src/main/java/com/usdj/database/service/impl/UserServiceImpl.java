package com.usdj.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usdj.database.dao.UserMapper;
import com.usdj.database.entity.User;
import com.usdj.database.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author gerrydeng
 * @date 2019-07-20 22:38
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
