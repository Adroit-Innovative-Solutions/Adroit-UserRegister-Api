package com.adroit.dto;

import com.adroit.model.UserType;
import jakarta.validation.constraints.*;
import java.util.Set;

public class UserRegisterDTO {

    @Size(max = 18, message = "User ID must be max 18 characters")
    private String userId;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String userName;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotEmpty(message = "Confirm password must not be empty")
    @Size(min = 8, max = 20, message = "Confirm password must be between 8 and 20 characters")
    private String confirmPassword;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email must not be empty")
    @Size(min = 5, max = 50, message = "Email must be between 10 and 50 characters")
    private String email;

    @NotEmpty(message = "Phone number must not be empty")
    // @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotNull(message = "At least one role is required")
    private Set<UserType> roles;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<UserType> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserType> roles) {
        this.roles = roles;
    }
}
