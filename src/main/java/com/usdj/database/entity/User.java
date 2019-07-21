package com.usdj.database.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

/**
 * @author gerrydeng
 * @date 2019-07-19 20:17
 * @Description:
 */
@Data
//public class User {
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User>{
    private static final long serialVersionUID = -8150965225155034704L;
    //    @TableId
    private Long id;


//    @TableField("name")
    // 通过实体查询可注解condition，属性为SqlCondition或者自定义
    // eg: condition = "%s&lt;#{%s}"
    // condition默认是等于
    @TableField(condition = SqlCondition.LIKE)
    private String name;

    private Integer age;

    private String email;

    private Long managerId;

    private LocalDateTime createTime;

//    不需要序列化的话，方式一
//    private transient String remark;

//    方式二，使用静态变量，类中只有一个
//    private static String remark;
//
//    public static String getRemark() {
//        return remark;
//    }
//
//    public static void setRemark(String remark) {
//        User.remark = remark;
//    }
//    方式二，使用注解
//    @TableField(exist = false)
//    private String remark;
}
