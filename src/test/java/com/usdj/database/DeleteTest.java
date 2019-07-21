package com.usdj.database;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usdj.database.dao.UserMapper;
import com.usdj.database.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gerrydeng
 * @date 2019-07-20 21:54
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void deleteById(){
        Assert.assertEquals(1, userMapper.deleteById(1152225527211339778L));
    }

    @Test
    public void deleteByMap(){
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("name", "小康");
        columnMap.put("age", 25);
        Assert.assertEquals(1, userMapper.deleteByMap(columnMap));
    }

    @Test
    public void deleteBatchIds(){
        Assert.assertEquals(2,userMapper.deleteBatchIds(Arrays.asList(1152224657572712449L,1152224089848528898L)));
    }

    @Test
    public void deleteByWrapper(){
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(User::getAge, 23).or().gt(User::getAge,41);
        int delete = userMapper.delete(lambdaQuery);
        System.out.println("Delete count:" + delete);
    }
}
