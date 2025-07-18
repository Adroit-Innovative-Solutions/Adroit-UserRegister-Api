package com.adroit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class ResetPasswordDto {

    private String userId;

    @NotNull(message = "OTP cannot be null")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
    private String otp;

    @NotNull(message = "Update Password cannot be null")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    private String UpdatePassword;

    @NotNull(message = "Confirm Password cannot be null")
    @Length(min = 8, message = "Confirm Password must be at least 8 characters long")
    private String ConfirmPassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUpdatePassword() {
        return UpdatePassword;
    }

    public void setUpdatePassword(String updatePassword) {
        UpdatePassword = updatePassword;
    }

}
