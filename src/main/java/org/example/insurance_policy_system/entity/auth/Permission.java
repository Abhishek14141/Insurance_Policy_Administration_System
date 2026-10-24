package org.example.insurance_policy_system.entity.auth;

import lombok.Data;
import jakarta.persistence.*;
@Entity @Table(name="permission_master")
@Data
public class Permission {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="permission_id") private Long id;
    @Column(name="permission_name",length=100,nullable=false) private String permissionName;
    @Column(name="module_name",length=100) private String moduleName;
    @Column(name="action_type",length=50) private String actionType;
    public Long getId(){
        return id;
    }
    public String getPermissionName(){
        return permissionName;
    }
    public void setPermissionName(String v){
        permissionName=v;
    }
    public String getModuleName(){
        return moduleName;
    }
    public void setModuleName(String v){
        moduleName=v;
    }
    public String getActionType(){
        return actionType;
    }
    public void setActionType(String v){
        actionType=v;
    }
}
