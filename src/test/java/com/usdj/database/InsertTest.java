package com.usdj.database;

import com.usdj.database.dao.UserMapper;
import com.usdj.database.entity.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * @author gerrydeng
 * @date 2019-07-21 13:07
 * @Description: mp测试用例
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InsertTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 插入基本实体类User
     */
    @Test
    public void insertTest(){
        User user = new User();
        user.setName("TestUser");
        user.setAge(20);
        user.setEmail("test@usdj.com");
        user.setManagerId(1087982257332887557L);
        user.setCreateTime(LocalDateTime.now());
        Assert.assertEquals(1, userMapper.insert(user));
    }

    /**
     * 插入带有非持久属性的实体类User
     */
    @Test
    public void insertWithFieldTest(){
        User user = new User();
        user.setName("TestUser123");
        user.setAge(20);
        user.setEmail("test@usdj.com");
        user.setManagerId(1087982257332887557L);
        user.setCreateTime(LocalDateTime.now());
//        user.setRemark("我不需要存");
        Assert.assertEquals(1, userMapper.insert(user));
    }

    /**
     * 在测试完后打印出所有
     */
    @After
    public void selectAllTest(){
        userMapper.selectList(null).forEach(System.out::println);
    }
}
