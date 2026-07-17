package io.microservices.exchangetoken.sourceapp.controller;

import io.microservices.exchangetoken.sourceapp.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static io.microservices.exchangetoken.sourceapp.constant.Constants.DEMO_PASSWORD_HASH;
import static io.microservices.exchangetoken.sourceapp.constant.Constants.DEMO_USER;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response) {
        if (!DEMO_USER.equals(username) || !passwordEncoder.matches(password, DEMO_PASSWORD_HASH)) {
            return "redirect:/login?error";
        }
        String token = jwtUtil.generateToken(username);
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);   // при HTTPS
        cookie.setPath("/");
        cookie.setMaxAge((int) jwtUtil.getExpirationMs() / 1000);
        response.addCookie(cookie);
        return "redirect:/home";
    }
}