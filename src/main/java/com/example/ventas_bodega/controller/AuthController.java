package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.request.ChangePasswordRequest;
import com.example.ventas_bodega.request.SignInRequest;
import com.example.ventas_bodega.request.SignUpRequest;
import com.example.ventas_bodega.response.SignInResponse;
import com.example.ventas_bodega.response.SignUpResponse;
import com.example.ventas_bodega.response.UserLoggedResponse;
import com.example.ventas_bodega.service.AuthService;
import com.example.ventas_bodega.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AuthController.API_PATH)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    public static final String API_PATH = "/api/auth";
    private final CookieUtil cookieUtil;

    public final AuthService authService;

    @Autowired
    public AuthController(AuthService authService, CookieUtil cookieUtil) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        logger.info("Intento de login para usuario: {}", signInRequest.getUsername());
        SignInResponse signInResponse = authService.signIn(signInRequest);
        if(signInResponse.getStatus() == 200) {
            logger.info("Login exitoso para usuario: {}", signInRequest.getUsername());

            Cookie cookie = cookieUtil.getCookie(signInResponse.getToken());
            response.addCookie(cookie);

            signInResponse.setToken(null);
            return new ResponseEntity<>(signInResponse, HttpStatus.OK);
        } else {
            logger.warn("Login fallido para usuario: {}", signInRequest.getUsername());
            return new ResponseEntity<>(signInResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    /*
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        logger.info("Intento de signUp para usuario: {}", signUpRequest.getEmail());
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);

        Cookie cookie = cookieUtil.getCookie(signUpResponse.getToken());
        response.addCookie(cookie);

        signUpResponse.setToken(null);
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }
    * */

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(HttpServletResponse response) {

        List<Cookie> cookiesList = List.of(
                new Cookie("auth_token", null),
                new Cookie("JSESSIONID", null)
        );

        for (Cookie cookie : cookiesList) {
            cookie.setHttpOnly(true);
            //cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided on cookie: Usuario no esta logueado");
        }
        UserLoggedResponse userLoggedResponse = authService.validateSession(token);
        return new ResponseEntity<>(userLoggedResponse, HttpStatus.OK);
    }

    @PutMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
