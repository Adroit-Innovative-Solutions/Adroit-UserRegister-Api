package com.adroit.dto;

import com.adroit.model.UserType;  // Correct import
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseDTO {

        private String userId;
        private String userName;
        private String email;
        private UserType roleType;
        private LocalDateTime loginTimestamp;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String userId, String userName, String email, UserType roleType, LocalDateTime loginTimestamp) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.roleType = roleType;
        this.loginTimestamp = loginTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getRoleType() {
        return roleType;
    }

    public void setRoleType(UserType roleType) {
        this.roleType = roleType;
    }

    public LocalDateTime getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(LocalDateTime loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }
}
