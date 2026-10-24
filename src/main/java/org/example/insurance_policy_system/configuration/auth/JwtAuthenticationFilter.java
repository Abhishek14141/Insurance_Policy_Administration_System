package org.example.insurance_policy_system.configuration.auth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    public JwtAuthenticationFilter(JwtService jwt){
        this.jwt=jwt;
    }
    @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException {
        String h=req.getHeader("Authorization");
        if(h!=null&&h.startsWith("Bearer ")){
            String token=h.substring(7);
            try{
                if(jwt.valid(token)&&SecurityContextHolder.getContext().getAuthentication()==null){
                    String username=jwt.username(token);
                    var auth=new UsernamePasswordAuthenticationToken(username,null,java.util.List.of());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch(Exception ignored){
            }
        }
        chain.doFilter(req,res);
    }
}
