package org.example.insurance_policy_system.entity.auth;

import lombok.Data;
import jakarta.persistence.*;
@Entity @Table(name="role_permission",uniqueConstraints=@UniqueConstraint(columnNames={"role_id","permission_id"}))
@Data
public class RolePermission {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="role_permission_id") private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="role_id") private Role role;
    @ManyToOne(optional=false) @JoinColumn(name="permission_id") private Permission permission;
    public Long getId(){
        return id;
    }
    public Role getRole(){
        return role;
    }
    public void setRole(Role v){
        role=v;
    }
    public Permission getPermission(){
        return permission;
    }
    public void setPermission(Permission v){
        permission=v;
    }
}
