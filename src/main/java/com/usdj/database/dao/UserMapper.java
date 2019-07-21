package com.usdj.database.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usdj.database.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gerrydeng
 * @date 2019-07-19 20:18
 * @Description:
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 测试lambda方法
     * @param wrapper
     * @return
     */
//    @Select("select * from user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    /**
     * 测试自定义分页方法
     * @param page
     * @param wrapper
     * @return
     */
    IPage<User> selectUserPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);
}
