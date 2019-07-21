package com.usdj.database;

import com.usdj.database.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * @author gerrydeng
 * @date 2019-07-20 22:06
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ARTest {

    @Test
    public void insert(){
        User user = new User();
        user.setName("AR");
        user.setAge(30);
        user.setEmail("test@123.com");
        user.setManagerId(1087982257332887555L);
        user.setCreateTime(LocalDateTime.now());
        boolean insert = user.insert();
        System.out.println("Insert:" + insert);
    }

    @Test
    public void select(){
        User user = new User();
        User select = user.selectById(1152581224822317058L);
        System.out.println(user == select);
        System.out.println("User"+ select);

    }

    @Test
    public void updateById(){
        User user = new User();
        user.setId(1152581224822317058L);
        user.setAge(22);
        boolean b = user.updateById();
        System.out.println("update:" + b);
    }

    @Test
    public void deleteById(){
        User user = new User();
        user.setId(1152581224822317058L);
        user.deleteById();
    }

    @Test
    public void insertOrUpdate(){
        User user = new User();
        user.setName("AR_new");
        user.setId(12314L);
        user.setAge(11);
        user.insertOrUpdate();
    }
}
