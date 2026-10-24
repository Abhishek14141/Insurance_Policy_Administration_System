package org.example.insurance_policy_system.controller.auth;
import org.example.authservice.util.ApiResponse;

import org.example.insurance_policy_system.dto.auth.RegisterRequest;
import org.example.insurance_policy_system.dto.auth.UserUpdateRequest;
import org.example.insurance_policy_system.dto.auth.UserView;
import org.example.insurance_policy_system.entity.auth.User;
import org.example.insurance_policy_system.service.auth.AdminService;
import org.example.insurance_policy_system.service.auth.AuthService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/users")
public class UserController {
    private final AdminService s;
    private final AuthService auth;
    public UserController(AdminService s,AuthService auth){
        this.s=s;
        this.auth=auth;
    }
    private UserView v(User u){
        return new UserView(u.getId(),u.getUsername(),u.getEmail(),u.getMobile(),u.getRole().getRoleName(),u.getStatus().name());
    }
    @PostMapping public ApiResponse<UserView> create(@Valid @RequestBody RegisterRequest q){
        return ApiResponse.ok("User created",auth.register(q));
    }
    @GetMapping public ApiResponse<?> all(){
        return ApiResponse.ok("Users fetched",s.users().stream().map(this::v).toList());
    }
    @GetMapping("/{id}") public ApiResponse<UserView> one(@PathVariable Long id){
        return ApiResponse.ok("User fetched",v(s.user(id)));
    }
    @PutMapping("/{id}") public ApiResponse<UserView> update(@PathVariable Long id,@Valid @RequestBody UserUpdateRequest q){
        return ApiResponse.ok("User updated",v(s.update(id,q)));
    }
    @DeleteMapping("/{id}") public ApiResponse<Void> delete(@PathVariable Long id){
        s.delete(id);
        return ApiResponse.ok("User deleted",null);
    }
    @PutMapping("/{id}/lock") public ApiResponse<UserView> lock(@PathVariable Long id){
        return ApiResponse.ok("User locked",v(s.lock(id)));
    }
    @PutMapping("/{id}/unlock") public ApiResponse<UserView> unlock(@PathVariable Long id){
        return ApiResponse.ok("User unlocked",v(s.unlock(id)));
    }
}
