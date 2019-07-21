package com.usdj.database;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
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
import java.util.List;
import java.util.Map;

/**
 * @author gerrydeng
 * @date 2019-07-21 13:25
 * @Description: MP不同方式查询测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RetrieveTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 简单查询所有，queryWrapper传入null即查询所有
     */
    @Test
    public void select(){
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(9, userList.size());
        userList.forEach(System.out::println);
    }

    /**
     * 通过id查询
     */
    @Test
    public void selectById() {
        User user = userMapper.selectById(1087982257332887553L);
        Assert.assertEquals(User.class, user.getClass());
    }

    /**
     * 通过批量id查询
     */
    @Test
    public void selectByIds() {
        List<Long> idsList = Arrays.asList(1152221669005324289L, 1152224089848528898L, 1152224657572712449L);
        userMapper.selectBatchIds(idsList).forEach(System.out::println);
    }

    /**
     * 通过Map查询,多个条件默认是与关系
     */
    @Test
    public void selectByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        // key是数据库中的列名
        columnMap.put("name", "小明");
        columnMap.put("age", 22);
        List<User> users = userMapper.selectByMap(columnMap);
        users.forEach(System.out::println);
    }


    /**
     * Wrapper查询
     * 场景1，查询姓李，年龄大于等于20岁的
     */
    @Test
    public void selectByWrapper1(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "李")
                .ge("age", 20);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * Wrapper
     * 场景2，查询名字含有王，年龄大于28，小于31，邮箱不为空的
     */
    @Test
    public void selectByWrapper2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "王")
                .lt("age", 31)
                .gt("age", 28)
                .isNotNull("email");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * Wrapper
     * 场景3，查询名字含有王，年龄20-40之间，邮箱不为空的
     */
    @Test
    public void selectByWrapper3(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "王")
                .between("age", 20, 40)
                .isNotNull("email");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * Wrapper
     * 场景4，查询名字含有小的，或者年龄大于25岁，按降序排列
     * SELECT id,name,age,email,manager_id,create_time FROM user WHERE name LIKE ? OR age >= ? ORDER BY age DESC
     */
    @Test
    public void selectByWrapper4(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "小")
                .or()
                .ge("age", 25)
                .orderByDesc("age");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 动态入参应该使用拼接sql,并指定占位符，防止sql注入
     * 场景5， 查询创建时间为2019-07-19并且上级名字为张的
     * SELECT id,name,age,email,manager_id,create_time FROM user WHERE date_format(create_time,'%Y-%m-%d') = ? AND manager_id IN (select id from user where name like '%张%')
     */
    @Test
    public void selectByWrapper5(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}","2019-07-19")
                .inSql("manager_id", "select id from user where name like '%张%'");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 错误使用
     * 场景5，假设没有指定占位符，sql注入可查询到所有数据,假设客户端可传入参数，传入带有or true，即会泄露数据
     */
    @Test
    public void selectByWrapper6(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d')","2019-07-19 or true")
                .inSql("manager_id", "select id from user where name like '%张%'");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 场景6 名字为张并且(年龄小于40或者邮箱不能为空）
     */
    @Test
    public void selectByWrapper7(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "张")
                .and(q -> q.lt("age", 40).isNotNull("email"));
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 场景7 名字为张或者(年龄小于35并且大于20并且邮箱不能为空
     * name like '张%' or (age < 35 and age > 20) and email is not null)
     */
    @Test
    public void selectByWrapper8(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "张")
                .or(q -> q.lt("age", 35)
                        .gt("age", 20)
                        .isNotNull("email"));
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 场景8 (年龄小于40或者邮箱不为空)并且姓张
     */
    @Test
    public void selectByWrapper9(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(q->q.lt("age", 40)
                .isNotNull("email"))
                .likeRight("name", "张");
        userMapper.selectList(queryWrapper).forEach(System.out::println);

    }

    /**
     * 场景9 查询年龄为22，25 27
     */
    @Test
    public void selectByWrapper10(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age", Arrays.asList(22,25,27));
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 默认情况下，查询会返回所有字段，但是我们需要返回特定的字段
     * 场景1 查询名字包含李且年龄小于40的人ID
     */
    @Test
    public void selectByWrapperReturn1(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").like("name", "李").lt("age", 40);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 情景2 查询名字包含李且年龄小于40，不显示mangerId和createTime
     */
    @Test
    public void selectByWrapperReturn2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(User.class, info -> !info.getColumn().equals("manager_id") &&
                !info.getColumn().equals("create_time")).like("name", "李")
                .lt("age", 40);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 当字段为特定状态时(为真)时，该查询字段才使用, condition
     */
    @Test
    public void selectByWrapperCondition() {
        String name = "张";
        String email = "";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 实体类进行条件查询
     */
    @Test
    public void selectByWrapperEntity() {
        User user = new User();
        user.setName("张三");
        user.setAge(10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * allEq
     */
    @Test
    public void selectByWrapperAllEq() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "张三");
        params.put("age", 10);
        queryWrapper.allEq((k, v) -> !k.equals("name"), params);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 返回map
     */
    @Test
    public void selectByWrapperMaps() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "小").lt("age", 40);
        userMapper.selectMaps(queryWrapper).forEach(System.out::println);
    }

    /**
     * 按照上级分组，查询每组的平均年龄，最大年龄，最小年龄，摒弃只需年龄总和小于500的组
     * SELECT avg(age) avg_age,min(age) min_age,max(age) max_age,manager_id FROM user GROUP BY manager_id HAVING sum(age) < ?
     */
    @Test
    public void selectByWrapperMaps2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age", "manager_id")
                .groupBy("manager_id").having("sum(age) < {0}", 500);
        userMapper.selectMaps(queryWrapper).forEach(System.out::println);
    }

    /**
     * 只会返回一个
     */
    @Test
    public void selectByWrapperObjs() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name").like("name", "李").lt("age", 40);
        userMapper.selectObjs(queryWrapper).forEach(System.out::println);
    }

    /**
     * 查询返回个数
     */
    @Test
    public void selectByWrapperCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "李").lt("age", 40);
        Integer integer = userMapper.selectCount(queryWrapper);
        System.out.println("总记录数:" + integer);
    }

    /**
     * 查询只有一条的，多余一条报错
     */
    @Test
    public void selectByWrapperOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "张").lt("age", 40);
        System.out.println("userMapper.selectOne(queryWrapper) = " + userMapper.selectOne(queryWrapper));
    }

    /**
     * lambda,可防止误写
     * SELECT id,name,age,email,manager_id,create_time FROM user WHERE name LIKE ? AND age < ?
     * lambdaQuery创建方法有三种
     * way 1：LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
     * way 2: LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
     * way 3: LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
     */
    @Test
    public void selectLambda() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getName, "李").lt(User::getAge, 40);
        userMapper.selectList(lambdaQuery).forEach(System.out::println);
    }

    @Test
    public void selectLambda2() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.likeRight(User::getName, "张").and(f -> f.lt(User::getAge, 40).isNotNull(User::getEmail));
        userMapper.selectList(lambdaQuery).forEach(System.out::println);
    }

    /**
     * 3.0.7后新增,可使用链式一步写完
     */
    @Test
    public void selectLambda3() {
        new LambdaQueryChainWrapper<>(userMapper).like(User::getName, "张")
                .lt(User::getAge, 40).list().forEach(System.out::println);
    }

    /**
     * 可自定义查询语句，这里以selectAll为例，需要先在UserMapper写该方法，语句书写有两种方式
     * 方式1： 在该方法上使用@Select("select * from user ${ew.customSqlSegment}")
     * 方式2： 在默认路径resources下mapper目录下新建xml文件，和mybatis一样
     */
    @Test
    public void selectLambda4() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.likeRight(User::getName, "张").and(f -> f.lt(User::getAge, 40).isNotNull(User::getEmail));
        userMapper.selectAll(lambdaQuery).forEach(System.out::println);
    }

    /**
     * 物理分页查询，需要先在配置类注入PaginationInterceptor类，
     * isSearchCount true默认为真时，会统计总页数，可设置为false,只返回特定列数
     */
    @Test
    public void selectPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 20);
        Page<User> page = new Page<>(1, 2, false);
        IPage<User> userIPage = userMapper.selectPage(page, queryWrapper);
        System.out.println("总页数：" + userIPage.getPages());
        System.out.println("总记录数：" + userIPage.getTotal());
        userIPage.getRecords().forEach(System.out::println);
    }

    @Test
    public void selectPageReturnMap() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 20);
        Page<User> userPage = new Page<>(1, 2);
        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(userPage, queryWrapper);
        System.out.println("总页数：" + mapIPage.getPages());
        System.out.println("总记录数：" + mapIPage.getTotal());
        mapIPage.getRecords().forEach(System.out::println);
    }

    /**
     * 自定义分页查询，同样是在UserMapper接口中写该方法
     */
    @Test
    public void selectMyPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 20);
        Page<User> userPage = new Page<>(1, 2);
        IPage<User> userIPage = userMapper.selectUserPage(userPage, queryWrapper);
        System.out.println("总页数：" + userIPage.getPages());
        System.out.println("总记录数：" + userIPage.getTotal());
        userIPage.getRecords().forEach(System.out::println);
    }
}
