package pers.fjl.healthcheck.service;

import pers.fjl.healthcheck.po.User;

public interface PubSubService {
    /**
     * Publish a message to gcp pub/sub
     * @param topic topic name
     * @param message message
     */
    void publishMessage(String topic, String message);

    /**
     * Send a verification link to user's email
     * @param user user entity
     */
    void sendVerificationLink(User user);
}
