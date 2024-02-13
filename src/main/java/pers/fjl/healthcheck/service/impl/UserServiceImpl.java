package pers.fjl.healthcheck.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pers.fjl.healthcheck.dao.UserDao;
import pers.fjl.healthcheck.dto.UserDTO;
import pers.fjl.healthcheck.po.User;
import pers.fjl.healthcheck.service.UserService;
import pers.fjl.healthcheck.vo.UserAddVO;
import pers.fjl.healthcheck.vo.UserUpdateVO;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDTO addUser(UserAddVO userAddVO) {
        boolean isUserExists = isUserExists(userAddVO.getUsername());
        if (isUserExists) {
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
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        } else {
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
    }

    @Override
    public UserDTO getUserInfo(String username) {
        // Get user by username
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .select(User::getId, User::getFirstName, User::getLastName, User::getUsername,User::getAccountCreated, User::getAccountUpdated);
        User userDB = userDao.selectOne(wrapper);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDB, userDTO);
        return userDTO;
    }

    private boolean isUserExists(String username){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userDao.selectCount(wrapper) != 0;
    }

    public void deleteUserIfExists(String username){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        userDao.delete(wrapper);
    }

}
