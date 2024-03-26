package pers.fjl.healthcheck.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pers.fjl.healthcheck.dao.UserDao;
import pers.fjl.healthcheck.dto.UserDTO;
import pers.fjl.healthcheck.po.User;
import pers.fjl.healthcheck.service.PubSubService;
import pers.fjl.healthcheck.service.TokenRecService;
import pers.fjl.healthcheck.service.UserService;
import pers.fjl.healthcheck.vo.UserAddVO;
import pers.fjl.healthcheck.vo.UserUpdateVO;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private PubSubService pubSubService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TokenRecService tokenRecService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDTO addUser(UserAddVO userAddVO) {
        boolean isUserExists = isUserExists(userAddVO.getUsername());
        if (isUserExists) {
            logger.error("User addition failed: Username {} already exists.", userAddVO.getUsername());
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = new User();
        BeanUtils.copyProperties(userAddVO, user);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAccountCreated(localDateTime);
        user.setAccountUpdated(localDateTime);

        if (userDao.insert(user) > 0) {   // successfully insert
            logger.info("User {} added successfully", user.getUsername());
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            pubSubService.sendVerificationLink(user);
            return userDTO;
        } else {
            logger.error("Failed to insert user {}", userAddVO.getUsername());
            return null;
        }
    }

    @Override
    public void update(String username, UserUpdateVO userUpdateVO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User userDB = userDao.selectOne(wrapper);
        userDB.setFirstName(userUpdateVO.getFirstName());
        userDB.setLastName(userUpdateVO.getLastName());
        userDB.setPassword(encoder.encode(userUpdateVO.getPassword()));
        userDB.setAccountUpdated(LocalDateTime.now());
        userDao.updateById(userDB);
        logger.info("User {} updated successfully", username);
    }

    @Override
    public UserDTO getUserInfo(String username) {
        logger.info("User {} try to retrieve his/her information", username);
        // Get user by username
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .select(User::getId, User::getFirstName, User::getLastName, User::getUsername, User::getAccountCreated, User::getAccountUpdated);
        User userDB = userDao.selectOne(wrapper);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDB, userDTO);
        return userDTO;
    }

    private boolean isUserExists(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userDao.selectCount(wrapper) != 0;
    }

    public void deleteUserIfExists(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        userDao.delete(wrapper);
    }

    @Override
    public boolean verifyEmail(String token, String username) {
        logger.info("User {} try to verify the email", username);
        boolean isTokenValid = tokenRecService.verifyToken(username, token);
        if (isTokenValid) {
            userDao.update(new User(), new LambdaUpdateWrapper<User>()
                    .set(User::isEmailVerified, true)
                    .eq(User::getUsername, username));
        }

        return isTokenValid;
    }

}
