package org.citic.iiot;

import org.citic.iiot.persist.Application;
import org.citic.iiot.persist.dao.user.UserDao;
import org.citic.iiot.persist.domain.user.User;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 用户登录test
 * Created by wangming on 2017/6/19
 */

@SpringBootTest(classes= Application.class)
@MybatisTest
@MapperScan("org.citic.iiot.persist.dao.user")
public class TestUserDao {

    @Autowired
    private UserDao userDao;

    @Test
    public void getCountByIdentifyName() {
        User user = userDao.findUserByUsername("prdadmin");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user);
    }

}
