package pers.fjl.healthcheck.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.healthcheck.dto.UserDTO;
import pers.fjl.healthcheck.po.User;
import pers.fjl.healthcheck.vo.UserAddVO;
import pers.fjl.healthcheck.vo.UserUpdateVO;

public interface UserService extends IService<User> {

    /**
     * Get user information
     * @param username username
     * @return userDTO
     */
    UserDTO getUserInfo(String username);

    /**
     * Create user
     * @param userAddVO add user entity
     * @return operation success flag
     */
    boolean addUser(UserAddVO userAddVO);

    /**
     * Update user info
     * @param username username
     * @param userUpdateVO update user entity
     */
    void update(String username, UserUpdateVO userUpdateVO);
}
