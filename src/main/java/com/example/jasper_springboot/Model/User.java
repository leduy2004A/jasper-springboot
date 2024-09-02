package com.example.jasper_springboot.Model;

import com.example.jasper_springboot.Request.UserRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Table(name = "user")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "fullname is require")
    private String fullname;
    @NotBlank(message = "username is require")
    private String username;
    @NotBlank(message = "email is require")
    @Email(message = "email is not valid")
    private String email;
    @Column(name = "created_at")
    private Date created_at;
    @Column(name = "update_at")
    private Date update_at;
    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    public User(UserRequest ur) {
        this.fullname = ur.getFullname();
        this.username = ur.getUsername();
        this.email = ur.getEmail();
        this.created_at = new Date();
        this.update_at = new Date();
    }
}
