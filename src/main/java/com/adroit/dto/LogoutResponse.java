package com.adroit.dto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LogoutResponse {

    private String userId;
    private String userName;
    private LocalDateTime logoutTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }
    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
}

