package com.adroit.controller;

import com.adroit.dto.*;
import com.adroit.model.Roles;
import com.adroit.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

//@CrossOrigin(origins = {"http://35.188.150.92", "http://192.168.0.140:3000",
//        "http://192.168.0.139:3002",
//        "http://localhost:3000","http://192.168.0.135:8080"})
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody UserRegisterDTO userDto) {

        UserResponse  userResponse=userService.registerUser(userDto);
        ApiResponse<UserResponse> apiResponse=new ApiResponse<>(
                true,
                "Registration Successful",
                userResponse,
                null
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO response = userService.authenticate(loginDTO);
        ApiResponse<LoginResponseDTO> apiResponse = new ApiResponse<>(
                true,
                "Login Successful",
                response,
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(@PathVariable String userId ) {

        System.out.println("User logged out: " + userId);
        LogoutResponse response=userService.logoutUser(userId);
        ApiResponse<LogoutResponse> apiResponse = new ApiResponse<>(true, "Logout successful", response, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
//    @PostMapping("/addusers")
//    public ResponseEntity<ResponseBean<UserResponse>> registerUsers(@Valid  @RequestBody UserDto userDto) throws RoleNotFoundException {
//
//        return   userService.registerUser(userDto);
//
//    }
    @GetMapping("/roles/{userId}")
    public ResponseEntity<Set<Roles>> getRolesByUserId(@PathVariable String userId ) {
        return userService.getRolesByUserId(userId);
    }
//    @PostMapping("/resetPassword")
//     public ResponseEntity<ApiResponse<ResetPasswordResponse>> changePassword(
//             @RequestBody ResetPasswordDto dto
//            ){
//
//
//    }
    @PostMapping("/generateOtp/{userId}")
    public ResponseEntity<String> generateOTP(
            @PathVariable String userId
    ){
       String response=userService.generateOTP(userId);

       return new ResponseEntity<>(response,HttpStatus.OK);
    }
//    @PutMapping("/updateUserDetails/{userId}")
//    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdateDTO userUpdateDto) throws RoleNotFoundException {
//
//        UserResponse userResponse=userService.updateUser(userId, userUpdateDto);
//
//        ApiResponse<UserResponse> apiResponse=new ApiResponse<>(true,"User Details Updated.",userResponse,null);
//        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
//    }

    @PutMapping("/updateUser")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserWithOutUserID(
            @RequestBody UserUpdateDTO dto
    ){
        UserResponse response=userService.updateUserWithoutUserId(dto);
        ApiResponse<UserResponse> apiResponse=new ApiResponse<>(true,"User Details Updated",response,null);

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(@PathVariable String userId) {

        UserResponse userResponse= userService.deleteUser(userId);
        ApiResponse<UserResponse> apiResponse=new ApiResponse<>(true,"User Deleted Successfully",userResponse,null);

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
    @GetMapping("/allUsers")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

    Pageable pageable= PageRequest.of(
            page,
            size);
            Page<UserDTO> response=userService.getAllUsers(pageable);

            PageResponse<UserDTO> pageResponse=new PageResponse<>(response);
            ApiResponse<PageResponse<UserDTO>> apiResponse=new ApiResponse<>(true,"Data Fetched",pageResponse,null);

            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUserID(
            @PathVariable String userId
    ){
        UserDTO response=userService.getUserById(userId);
        ApiResponse<UserDTO> apiResponse=new ApiResponse<>(true,"User Data Fetched",response,null);

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }


}
