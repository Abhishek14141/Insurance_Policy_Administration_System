package org.example.insurance_policy_system.entity.auth;

import lombok.Data;
import jakarta.persistence.*;
import org.example.insurance_policy_system.enums.auth.UserStatus;

@Entity @Table(name="user_master")
@Data
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="user_id") private Long id;
    @Column(nullable=false,unique=true,length=100) private String username;
    @Column(name="password_hash",nullable=false,length=255) private String passwordHash;
    @Column(length=150,unique=true) private String email;
    @Column(length=20,unique=true) private String mobile;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="role_id",nullable=false) private Role role;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private UserStatus status=UserStatus.Active;
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public String getPasswordHash(){
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash=passwordHash;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getMobile(){
        return mobile;
    }
    public void setMobile(String mobile){
        this.mobile=mobile;
    }
    public Role getRole(){
        return role;
    }
    public void setRole(Role role){
        this.role=role;
    }
public UserStatus getStatus(){
        return status;
    }
    public void setStatus(UserStatus status){
        this.status=status;
    }
}
