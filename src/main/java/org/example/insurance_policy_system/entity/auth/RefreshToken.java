package org.example.insurance_policy_system.entity.auth;

import lombok.Data;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="refresh_token")
@Data
public class RefreshToken {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true,length=500) private String token;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id") private User user;
    @Column(nullable=false) private Instant expiresAt;
    @Column(nullable=false) private boolean revoked=false;
    public Long getId(){
        return id;
    }
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token=token;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }
    public Instant getExpiresAt(){
        return expiresAt;
    }
    public void setExpiresAt(Instant expiresAt){
        this.expiresAt=expiresAt;
    }
    public boolean isRevoked(){
        return revoked;
    }
    public void setRevoked(boolean revoked){
        this.revoked=revoked;
    }
}
