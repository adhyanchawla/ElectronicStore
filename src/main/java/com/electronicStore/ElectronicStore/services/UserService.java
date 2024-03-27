package com.electronicStore.ElectronicStore.services;

import com.electronicStore.ElectronicStore.dtos.PageableResponse;
import com.electronicStore.ElectronicStore.dtos.UserDTO;

import java.io.IOException;
import java.util.List;

public interface UserService {

    // create
    UserDTO createUser(UserDTO userDTO);

    // update
    UserDTO updateUser(UserDTO userDTO, String userId);

    // delete
    void deleteUser(String userId) throws IOException;

    //get all users
    PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //search user
    List<UserDTO> searchUser(String keyword);

    // get user by id
    UserDTO getUserById(String userId);

    //get user by email
    UserDTO getUserByEmail(String userId);

    //other user specific
}
