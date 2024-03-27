package com.electronicStore.ElectronicStore.controllers;

import com.electronicStore.ElectronicStore.dtos.ApiResponseMessage;
import com.electronicStore.ElectronicStore.dtos.ImageResponse;
import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.dtos.UserDTO;
import com.electronicStore.ElectronicStore.services.FileService;
import com.electronicStore.ElectronicStore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger  logger = LoggerFactory.getLogger(UserController.class);

    // create
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO userDTO1 = this.userService.createUser(userDTO);
        return new ResponseEntity<>(userDTO1, HttpStatus.CREATED);
    }

    // update
    @PostMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable String id) {
        UserDTO userDTO1 = this.userService.updateUser(userDTO, id);
        return new ResponseEntity<>(userDTO1, HttpStatus.OK);
    }

    // get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDTO>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<UserDTO> userDTOS = this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    // get single
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        UserDTO userDTO = this.userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO userDTO = this.userService.getUserByEmail(email);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDTO>> searchUsers(@PathVariable String keywords) {
        List<UserDTO> userDTOS = this.userService.searchUser(keywords);
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String id) throws IOException {
        this.userService.deleteUser(id);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User deleted successfully!!!")
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("userImage")MultipartFile image,
            @PathVariable String userId
            ) throws IOException {
        String imageName = fileService.uploadFile(image, imagePath);
        UserDTO userDTO = userService.getUserById(userId);
        userDTO.setImageName(imageName);
        userService.updateUser(userDTO, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName)
                .httpStatus(HttpStatus.OK)
                .message("Image Uploaded Successfully!!").build();
        return new ResponseEntity<>(imageResponse, HttpStatus.OK);
    }

    // serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDTO userDTO = userService.getUserById(userId);
        logger.info("User Image Name: {}", userDTO.getImageName());
        InputStream resource = fileService.getResource(imagePath, userDTO.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
