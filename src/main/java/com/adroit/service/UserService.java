
package com.adroit.service;

import com.adroit.dto.*;
import com.adroit.exceptions.*;
import com.adroit.model.Roles;
import com.adroit.model.UserDetails;
import com.adroit.model.UserType;
import com.adroit.repository.RolesDao;
import com.adroit.repository.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RolesDao rolesDao;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    public UserResponse registerUser(UserRegisterDTO userDto)  {

        logger.info("Registering new User :"+userDto.getUserId());
        if (userDao.findByEmail(userDto.getEmail()) != null) {
            throw new EmailAlreadyExistsException(userDto.getEmail()+" is already in use");
        }
        if (userDao.findByUserId(userDto.getUserId()) != null) {
            throw new UserAlreadyExistsException(userDto.getUserId() + " already exists. Please log in");
        }

        if(!userDto.getPassword().equals(userDto.getConfirmPassword())){
            throw new PasswordMismatchException("Password and Confirm Password do not match. Please ensure both fields have the same value.");
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        UserDetails user = new UserDetails();
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());

        Set<Roles> roles = userDto.getRoles().stream()
                .map(role -> {
                        return rolesDao.findByRole(role)
                                .orElseThrow(() -> new RoleNotFoundException("No matching role found in the system for: " + role));
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setStatus("ACTIVE");

        UserDetails dbUser = userDao.save(user);
        logger.info("User Registered Successfully :"+userDto.getUserId());

        UserResponse response = new UserResponse();
        response.setUserName(dbUser.getUserName());
        response.setUserId(dbUser.getUserId());
        response.setEmail(dbUser.getEmail());

        sendRegistrationConfirmationEmail(dbUser.getEmail());

        return response;
    }
    public void sendRegistrationConfirmationEmail(String email) {
        String subject = "Welcome to MyMulya – Registration Successful!";

        String body = """
        <html>
        <body>
            <p>Dear User,</p>
            <p>Thank you for registering with <strong>MyMulya</strong>!</p>
            <p>We’re excited to have you onboard. You can now explore our platform and make the most of its features.</p>
            <p>If you did not initiate this registration, please ignore this email or contact our support team immediately.</p>
            <br>
            <p>Best regards,</p>
            <p><strong>The MyMulya Team</strong></p>
            <hr>
            <small>This is an automated message. Please do not reply to this email.</small>
        </body>
        </html>
        """;

        emailService.sendEmail(email, subject, body);
    }

    public ResponseEntity<Set<Roles>> getRolesByUserId(String UserId ) {
        UserDetails user = userDao.findByUserId(UserId);
        Set<Roles> roles = user.getRoles();
        return  ResponseEntity.ok(roles);

    }

    public UserResponse updateUserWithoutUserId(UserUpdateDTO updateDTO){

        UserDetails existingUserDetails=userDao.findByUserId(updateDTO.getUserId());

        if(existingUserDetails==null) throw new UserNotFoundException("No User Found With ID :"+updateDTO.getUserId());

        UserDetails updatedUser=updateExistingUser(existingUserDetails,updateDTO);

        userDao.save(updatedUser);

        UserResponse response=new UserResponse();
        response.setUserId(updateDTO.getUserId());
        response.setUserName(updatedUser.getUserName());
        response.setEmail(updatedUser.getEmail());

        return response;
    }

    public UserDetails updateExistingUser(UserDetails existingUser, UserUpdateDTO updateUser) {
        if (updateUser.getUserName() != null) existingUser.setUserName(updateUser.getUserName());
        if (updateUser.getStatus() != null) existingUser.setStatus(updateUser.getStatus());
        if (updateUser.getGender() != null) existingUser.setGender(updateUser.getGender());
        if (updateUser.getDesignation() != null) existingUser.setDesignation(updateUser.getDesignation());
        if (updateUser.getDob() != null) existingUser.setDob(updateUser.getDob());
        if (updateUser.getPersonalEmail() != null) existingUser.setPersonalEmail(updateUser.getPersonalEmail());
        if (updateUser.getJoiningDate() != null) existingUser.setJoiningDate(updateUser.getJoiningDate());
        if (updateUser.getPhoneNumber() != null) existingUser.setPhoneNumber(updateUser.getPhoneNumber());
        if (updateUser.getRoles()!=null) existingUser.setRoles(updateUser.getRoles());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return existingUser;
    }


    public LoginResponseDTO authenticate(LoginDTO loginDTO) {

        UserDetails userDetails = userDao.findByEmail(loginDTO.getEmail());
        if (userDetails == null) {
            throw new UserNotFoundException("User not found with email: " + loginDTO.getEmail());
        }
        if (!"ACTIVE".equals(userDetails.getStatus())) {
            throw new UserInactiveException("Your account is currently inactive. Please contact the administrator.");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        if (userDetails.getRoles() == null || userDetails.getRoles().isEmpty()) {
            throw new InvalidCredentialsException("No roles assigned to the user");
        }
        userDetails.setLastLoginTime(LocalDateTime.now());

        userDao.save(userDetails);

        UserType roleType = userDetails.getRoles().iterator().next().getRole();

        LoginResponseDTO response = new LoginResponseDTO(
                userDetails.getUserId(),
                userDetails.getUserName(),
                userDetails.getEmail(),
                roleType,
                userDetails.getLastLoginTime()
        );

        return response;
    }
    public LogoutResponse logoutUser(String userId){
        UserDetails user=userDao.findByUserId(userId);
        if (user==null) throw new UserNotFoundException("User Not Found With ID :"+userId);

        user.setLastLogoutTime(LocalDateTime.now());
        userDao.save(user);
        LogoutResponse response=new LogoutResponse();
        response.setUserId(userId);
        response.setUserName(user.getUserName());
        response.setLogoutTime(LocalDateTime.now());

    return response;
   }
//   public ResetPasswordResponse resetPassword(ResetPasswordDto dto){
//
//       UserDetails userDetails= userDao.findByUserId(dto.getUserId());
//       if(userDetails==null) throw new UserNotFoundException("No User Found With ID :"+dto.getUserId());
//       else{
//
//       }
//   }

   private final Map<String,String> otpStorageOnUserId=new ConcurrentHashMap<>();
   private final Map<String,Long> otpTimeStamps=new ConcurrentHashMap<>();
   private final Random random=new Random();
   private static final long OTP_EXPIRY_TIME_MS= 5 * 60 * 1000; // 5min
   private static final long OTP_COOLDOWN_MS=60*1000; // 1min

    public String generateOTP(String userId){
        UserDetails userDetails= userDao.findByUserId(userId);
        if(userDetails==null) throw new UserNotFoundException("No User Found With ID :"+userId);
        else{
            String otp=String.format("%06d",random.nextInt(999999));
            otpStorageOnUserId.put(userId,otp);
            otpTimeStamps.put(userId,System.currentTimeMillis());
            sendOtpEmail(userDetails.getEmail(),otp);

            logger.info("OtpTimeStamp in milliseconds :"+otpTimeStamps.get(userId));
            logger.info("OtpStorageOnUserID in milliseconds :"+otpStorageOnUserId.get(userId));

        }
        return "Success";
    }
    public void sendOtpEmail(String email, String otp) {
        String subject = "MyMulya – OTP for Password Reset";
        String body = "<html>\n" +
                "<body>\n" +
                "    <p>Dear User,</p>\n" +
                "    <p>We received a request to reset your password on <strong>MyMulya</strong>.</p>\n" +
                "    <p>Please use the following One-Time Password (OTP) to proceed:</p>\n" +
                "    <h2 style=\"color:#2E86C1;\">" + otp + "</h2>\n" +
                "    <p>This OTP is valid for 10 minutes. Do not share it.</p>\n" +
                "</body>\n" +
                "</html>";
        emailService.sendEmail(email, subject, body);
    }
    public UserResponse deleteUser(String userId) {
        UserDetails user = userDao.findByUserId(userId);
        if (user == null) {
           throw new UserNotFoundException("No User Found with ID "+userId);
        }
        userDao.delete(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setUserName(user.getUserName());
        userResponse.setEmail(user.getEmail());

        return userResponse;
    }

    public UserDTO getUserById(String userId) {
        UserDetails user = userDao.findByUserId(userId);
        if (user==null) {
            throw new UserNotFoundException("Recruiter not found with ID: " + userId);
        }
        UserDTO dto=convertToDTO(user);
        return dto;
    }

    public UserDTO convertToDTO(UserDetails user)  {
        UserDTO dto = new UserDTO();

        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPersonalEmail(user.getPersonalEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDesignation(user.getDesignation());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        dto.setJoiningDate(user.getJoiningDate());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setRoles(user.getRoles().stream().map(Roles::getRole).collect(Collectors.toSet()));

        return dto;
    }
    public Page<UserDTO> getAllUsers(Pageable pageable){

        Page<UserDetails> userDetails=userDao.findAll(pageable);
        Page<UserDTO> userDTOS=userDetails.map(this::convertToDTO);

        return userDTOS;
    }
}

