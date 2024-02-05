package pers.fjl.healthcheck;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JasyptGenerator {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    void contextLoads() {
    }

    @Test
    public void generateEncryptCredentials() {
        String url = encryptor.encrypt("");
        String username = encryptor.encrypt("");
        String password = encryptor.encrypt("");
        System.out.println("url:" + url);
        System.out.println("username:" + username);
        System.out.println("password:" + password);
    }

}
