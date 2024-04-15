package pers.fjl.healthcheck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pers.fjl.healthcheck.dto.UserDTO;
import pers.fjl.healthcheck.entity.Result;
import pers.fjl.healthcheck.service.UserService;
import pers.fjl.healthcheck.vo.UserAddVO;
import pers.fjl.healthcheck.vo.UserUpdateVO;

import javax.validation.Valid;
import java.util.Objects;

@Validated
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/v2/user")
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserAddVO userAddVO) {
        UserDTO userDTO = userService.addUser(userAddVO);
        if (!Objects.isNull(userDTO)) {
            return Result.ok(HttpStatus.CREATED, userDTO);
        } else {
            return Result.fail(HttpStatus.BAD_REQUEST, "Username already exists");
        }
    }

    // If current user can successfully pass the spring security authentication, we can get its info from SecurityContextHolder
    @GetMapping("/v2/user/self")
    public ResponseEntity<Object> getUserInfo(@RequestBody(required = false) String requestBody) {
        // Check if the request body has parameters
        if (StringUtils.hasText(requestBody)) {
            return Result.fail(HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO userDTO = userService.getUserInfo(username);
        return Result.ok(userDTO);
    }

    @PutMapping("/v2/user/self")
    public ResponseEntity<Object> updateUserInfo(@Valid @RequestBody UserUpdateVO userUpdateVO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.update(username, userUpdateVO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Object> verifyEmail(@RequestParam("token") String token, @RequestParam("username") String username) {
        boolean isVerified = userService.verifyEmail(token, username);
        if (isVerified){
            return Result.ok("Successfully verified user:" + username);
        }else{
            return Result.fail(HttpStatus.GONE, "The verification link is expired or incorrect");
        }
    }

}
