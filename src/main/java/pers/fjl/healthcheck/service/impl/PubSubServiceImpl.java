package pers.fjl.healthcheck.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Service;
import pers.fjl.healthcheck.po.EmailLog;
import pers.fjl.healthcheck.po.User;
import pers.fjl.healthcheck.service.PubSubService;
import pers.fjl.healthcheck.service.TokenRecService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PubSubServiceImpl implements PubSubService {

    @Autowired
    private TokenRecService tokenRecService;
    @Value("${pubsub.email-topic}")
    private String topic;
    @Value("${hostname}")
    private String hostname;

    private static final Logger logger = LoggerFactory.getLogger(PubSubServiceImpl.class);

    private final PubSubTemplate pubSubTemplate;

    public PubSubServiceImpl(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }

    public void sendVerificationLink(User user) {
        // Generate verification link
        String token = UUID.randomUUID().toString();
        String verificationLink = "https://" + hostname + "/verify-email?username=" + user.getUsername() + "&token=" + token;
        String content = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<p style='font-size: 16px;'>Hi " + user.getFirstName() + ",</p>" +
                "<p>Please click <a href='" + verificationLink + "' style='color: #3498db;'>this link</a> within 2 minutes to activate your account. If you cannot click the link, please copy and paste the following URL into your browser:</p>" +
                "<p><code>" + verificationLink + "</code></p>" +
                "<p>Thank you!</p>" +
                "</body></html>";

        EmailLog emailLog = EmailLog.builder()
                .id(UUID.randomUUID().toString())
                .recipient(user.getUsername())
                .sender("Jiale Fang Site Support <no-reply@jialefang.site>")
                .subject("Activate your email account")
                .content(content)
                .createTime(LocalDateTime.now())
                .isSent(false)
                .errMsg("")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String messageJson = objectMapper.writeValueAsString(emailLog);
            boolean inserted = tokenRecService.addTokenRec(user.getUsername(), token);
            if (!inserted) {
                return;
            }
            publishMessage(topic, messageJson);
        } catch (JsonProcessingException e) {
            logger.error("Can not parse user {} into json", user, e);
            throw new RuntimeException(e);
        } catch (io.grpc.StatusRuntimeException e) {
            logger.error("Can not send message to pub/sub", e);
            throw new RuntimeException(e);
        }
    }

    public void publishMessage(String topic, String message) {
        this.pubSubTemplate.publish(topic, message);
    }

}
