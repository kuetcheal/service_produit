package com.javathinked.example.demo_spring.controller;
import com.javathinked.example.demo_spring.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        // Option B : on accepte n'importe quel username si le mot de passe est "admin123"
        if (req.getPassword() == null || !req.getPassword().equals("admin123")) {
            return ResponseEntity.status(401).build();
        }
        List<String> roles = List.of("ROLE_USER");
        String token = jwtUtil.generate(req.getUsername(), roles);
        return ResponseEntity.ok(new AuthResponse(token, req.getUsername(), roles));
    }

    // ===== DTOs =====
    public static class LoginRequest {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private String username;
        private List<String> roles;
        public AuthResponse(String token, String username, List<String> roles) {
            this.token = token; this.username = username; this.roles = roles;
        }
        public String getToken() { return token; }
        public String getUsername() { return username; }
        public List<String> getRoles() { return roles; }
    }
}
