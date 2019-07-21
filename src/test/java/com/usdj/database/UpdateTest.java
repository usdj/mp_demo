package com.usdj.database;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.usdj.database.dao.UserMapper;
import com.usdj.database.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author gerrydeng
 * @date 2019-07-20 20:09
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateById(){
        User user = new User();
        user.setId(1087982257332887557L);
        user.setAge(18);
        Assert.assertEquals(1, userMapper.updateById(user));
    }

    @Test
    public void updateByWrapper1(){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "小李").eq("age", 24);
        User user = new User();
        user.setEmail("hello@test.com");
        user.setAge(21);
        Assert.assertEquals(1, userMapper.update(user, updateWrapper));
    }

    @Test
    public void updateByWrapper2(){
        User whereUser = new User();
        whereUser.setName("小李");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>(whereUser);
        updateWrapper.eq("age", 21);
        User user = new User();
        user.setEmail("hello@test.com");
        user.setAge(22);
        Assert.assertEquals(1, userMapper.update(user, updateWrapper));
    }

    @Test
    public void updateByWrapper3(){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "小李").eq("age", 22).set("age", 30);
        Assert.assertEquals(1, userMapper.update(null, updateWrapper));
    }

    @Test
    public void updateByWrapperLambda(){
        LambdaUpdateWrapper<User> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.eq(User::getName, "小李").eq(User::getAge, 30).set(User::getAge, 31);
        Assert.assertEquals(1, userMapper.update(null, lambdaUpdate));
    }

    @Test
    public void updateByWrapperLambdaChain(){
        boolean isUpdate = new LambdaUpdateChainWrapper<>(userMapper)
                .eq(User::getName, "小李").eq(User::getAge, 32).set(User::getAge, 33).update();
        Assert.assertTrue(isUpdate);
    }
}
