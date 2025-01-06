package com.test.lyl.test.vo;


/**
 *  用户访问令牌
 */

public class Token {


    /**
     * 用户id
     */
    private Integer userId;

    /**
     * session的自发串
     */
    private String sessionKey;


    /**
     * 颁发的时间
     */
    private Long  pubTime;

    public Token(Integer userId, String sessionKey, Long pubTime) {
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.pubTime = pubTime;
    }



    public Token() {

    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserid(Integer userId) {
        this.userId = userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getPubTime() {
        return pubTime;
    }

    public void setPubTime(Long pubTime) {
        this.pubTime = pubTime;
    }

    public boolean isExpired(){
        long currentTime = System.currentTimeMillis();

        if(currentTime - this.pubTime > 10 * 60 * 1000){
            return true;
        }
        return false;

    }
}
