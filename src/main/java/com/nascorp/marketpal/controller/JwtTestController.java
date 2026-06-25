// package com.nascorp.marketpal.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import jakarta.servlet.http.HttpServletRequest;

// import java.util.Map;

// @RequestMapping("/jwt")
// @RestController
// public class JwtTestController {
    
//     @GetMapping("/test")
//     public ResponseEntity<?> testProtected(HttpServletRequest request) {
        
//         String username = SecurityContextHolder.getContext()
//                           .getAuthentication()
//                           .getName();
//         return ResponseEntity.ok(Map.of("message", "You are authenticated!", "username", username));
//     }
// }


//                                       DANGER 💀
//                            ****JWT Token Debugging and Testing File****
//                                    Don't Touch It!!!
