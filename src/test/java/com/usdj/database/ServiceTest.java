package com.usdj.database;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usdj.database.entity.User;
import com.usdj.database.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author gerrydeng
 * @date 2019-07-20 22:40
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getOne(){
        System.out.println(userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 25), false));
    }

    @Test
    public void batch(){
        User user1 = new User();
        user1.setName("Test1");
        user1.setAge(20);
        User user2 = new User();
        user2.setName("Test2");
        user2.setAge(22);
        List<User> users = Arrays.asList(user1, user2);
        System.out.println(userService.saveBatch(users));
    }

    @Test
    public void chain(){
        userService.lambdaQuery().gt(User::getAge, 25).like(User::getName, "李").list().forEach(System.out::println);
    }
}
