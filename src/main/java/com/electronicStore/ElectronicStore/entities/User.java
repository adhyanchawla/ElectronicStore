package com.electronicStore.ElectronicStore.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    private String userId;
    @Column(name ="name")
    private String name;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name ="user_password", length = 10)
    private String password;

    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name = "user_image")
    private String imageName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();
}
