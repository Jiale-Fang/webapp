package pers.fjl.healthcheck.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import pers.fjl.healthcheck.dao.UserDao;
import pers.fjl.healthcheck.po.User;
import pers.fjl.healthcheck.po.UserDetailDTO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user;
        try {
            user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        } catch (Exception e) {
            logger.error("Unable to connect to the database", e);
            throw new PersistenceException(e);
        }

        if (ObjectUtils.isEmpty(user)) {
            logger.warn("User verification failed: Can't found a corresponding user: {}", username);
            throw new UsernameNotFoundException("Can't found a corresponding user");
        }

        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(user, userDetailDTO);

        return userDetailDTO;
    }

}
