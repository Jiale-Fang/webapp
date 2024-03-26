package pers.fjl.healthcheck.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.fjl.healthcheck.dao.TokenRecDao;
import pers.fjl.healthcheck.po.TokenRec;
import pers.fjl.healthcheck.service.TokenRecService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class TokenRecServiceImpl implements TokenRecService {

    private static final Logger logger = LoggerFactory.getLogger(TokenRecServiceImpl.class);

    @Autowired
    private TokenRecDao tokenRecDao;

    @Override
    public boolean addTokenRec(String username, String token) {
        TokenRec tokenRec = TokenRec.builder()
                .id(UUID.randomUUID().toString())
                .token(token)
                .username(username)
                .createTime(LocalDateTime.now()).build();

        if (tokenRecDao.insert(tokenRec) > 0) {   // successfully insert
            logger.info("User {} 's Token {} added successfully", username, token);
            return true;
        } else {
            logger.error("Failed to insert User {} 's Token {}", username, token);
            return false;
        }
    }

    @Override
    public boolean verifyToken(String username, String token) {
        LocalDateTime currentTime = LocalDateTime.now();

        TokenRec tokenRecDB = tokenRecDao.selectOne(new LambdaQueryWrapper<TokenRec>().eq(TokenRec::getUsername, username).eq(TokenRec::getToken, token));

        if (Objects.isNull(tokenRecDB)) {
            logger.warn("Can not find corresponding a user {} match the token {}", username, token);
            return false;
        }

        LocalDateTime createTime = tokenRecDB.getCreateTime();
        Duration duration = Duration.between(createTime, currentTime);

        if (duration.getSeconds() > 120) {
            logger.error("Token is expired, can not activate user {}", username);
            return false;
        }

        return true;
    }


}
