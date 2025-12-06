package com.main.jobilitybackend.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
import com.main.jobilitybackend.entities.Student;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.jwtSecurity.Jwtconstants;
import com.main.jobilitybackend.repositories.StudentRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        StudentResponse student = studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);

        if (student == null) {
            Student student2 = new Student();
            student2.setId(UUID.randomUUID().toString());
            student2.setEmail(email);
            if(name != null && !name.isEmpty()) {
                student2.setFirstName(name.split(" ")[0]);
                student2.setLastName(name.split(" ").length > 1 ? name.split(" ")[1] : "");
            }
            studentRepo.save(student2);
        }

        String userAgent = request.getHeader("User-Agent");
            long expirationTime = Jwtconstants.WEBEXPIRATION;
            if (userAgent != null) {
                if (userAgent.toLowerCase().contains("android")) {
                   expirationTime = Jwtconstants.ANDROIDEXPIRATION;
                } 
            }

        String jwtToken = JwtProvider.generateJwtToken(authentication,expirationTime);

        Cookie cookie = new Cookie("accessToken", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int)expirationTime); // 1 day
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);

        response.sendRedirect("http://localhost:5173/oauth2/redirect");
    }
}
