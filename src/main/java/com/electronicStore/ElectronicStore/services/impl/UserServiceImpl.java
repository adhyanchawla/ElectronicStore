package com.electronicStore.ElectronicStore.services.impl;

import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.dtos.UserDTO;
import com.electronicStore.ElectronicStore.entities.User;
import com.electronicStore.ElectronicStore.exceptions.ResourceNotFoundException;
import com.electronicStore.ElectronicStore.repositories.UserRepo;
import com.electronicStore.ElectronicStore.services.UserService;
import com.electronicStore.ElectronicStore.utilities.Helper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        String userId = UUID.randomUUID().toString();
        userDTO.setUserId(userId);
        User user = dtoToEnitity(userDTO);
        this.userRepo.save(user);
        return entityToDto(user);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ID not found"));
        user.setName(userDTO.getName());
        user.setAbout(userDTO.getAbout());
        user.setGender(userDTO.getGender());
//        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setImageName(userDTO.getImageName());
        User updatedUser = this.userRepo.save(user);
        return entityToDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) throws IOException {
        User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ID not found"));
        // delete user profile image
        String imageName = user.getImageName();
        String fullPath = imagePath + imageName;

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch(NoSuchFileException ex) {
            logger.info("No such file found!");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // delete user

        this.userRepo.delete(user);
    }

    @Override
    public PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = this.userRepo.findAll(pageable);
        PageableResponse<UserDTO> response = Helper.getPageableResponse(page, UserDTO.class);
        return response;
    }

    @Override
    public List<UserDTO> searchUser(String keyword) {
        List<User> users = userRepo.findByNameContaining(keyword);
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ID not found!!!"));
        return entityToDto(user);
    }

    @Override
    public UserDTO getUserByEmail(String mail) {
        User user = this.userRepo.findByEmail(mail).orElseThrow(()->new ResourceNotFoundException("User Not Found with the email!!!"));
        return entityToDto(user);
    }

    public User dtoToEnitity(UserDTO userDTO) {
        User user = this.modelMapper.map(userDTO, User.class);
//        User user = User.builder()
//                .userId(userDTO.getUserId())
//                .email(userDTO.getEmail())
//                .gender(userDTO.getGender())
//                .password(userDTO.getPassword())
//                .imageName(userDTO.getImageName())
//                .about(userDTO.getAbout())
//                .build();
        return user;
    }

    public UserDTO entityToDto(User user) {
        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
//        UserDTO userDTO = UserDTO.builder()
//                .userId(user.getUserId())
//                .email(user.getEmail())
//                .gender(user.getGender())
//                .password(user.getPassword())
//                .imageName(user.getImageName())
//                .about(user.getAbout())
//                .build();
        return userDTO;
    }
}
