package pers.fjl.healthcheck.service;

public interface TokenRecService {

    /**
     * Insert username and token record
     * @param username user's email
     * @param token verification link's token
     * @return insert successfully or not
     */
    boolean addTokenRec(String username, String token);

    /**
     * Verify token
     * @param username user's email
     * @param token verification link's token
     * @return token is valid or not
     */
    boolean verifyToken(String username, String token);
}
