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
     * @return userDTO
     */
    UserDTO addUser(UserAddVO userAddVO);

    /**
     * Update user info
     * @param username username
     * @param userUpdateVO update user entity
     */
    void update(String username, UserUpdateVO userUpdateVO);

    /**
     * Delete user by username
     * @param username username
     */
    void deleteUserIfExists(String username);

    /**
     * Verify user's email base on token
     * @param token unique token
     * @param username email
     * @return User is successfully verified or not
     */
    boolean verifyEmail(String token, String username);
}
