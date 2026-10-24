package org.example.insurance_policy_system.entity.auth;

import lombok.Data;
import jakarta.persistence.*;
@Entity @Table(name="role_master")
@Data
public class Role {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="role_id") private Long id;
    @Column(name="role_name",nullable=false,unique=true,length=100) private String roleName;
    @Column(length=255) private String description;
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getRoleName(){
        return roleName;
    }
    public void setRoleName(String roleName){
        this.roleName=roleName;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
}
