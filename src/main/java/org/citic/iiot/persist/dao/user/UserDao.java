package org.citic.iiot.persist.dao.user;

import org.citic.iiot.persist.dao.MapperDaoTemplate;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.citic.iiot.persist.domain.user.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao extends MapperDaoTemplate<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    public UserDao(SqlSessionTemplate sqlSessionTemplate){
        super(sqlSessionTemplate);
    }

    public User findUserByUsername(String username) {
        User user = new User();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        User userResult = this.selectOne(user, params);
        logger.debug("The user count ={}", userResult);
        logger.info("The user count ={}", userResult);
        return userResult;
    }

}
