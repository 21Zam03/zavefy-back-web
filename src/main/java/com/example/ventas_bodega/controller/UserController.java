package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.CreateUserRequest;
import com.example.ventas_bodega.request.UpdateUserRequest;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.API_PATH)
public class UserController {

    public static final String API_PATH = "/api/user";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(userService.getUsersByCompany(user, searchKey, page, size), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getAssignableRoles() {
        return new ResponseEntity<>(userService.getAssignableRoles(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(userService.createUser(request, user), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(userService.updateUser(request, user), HttpStatus.OK);
    }

    @PatchMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestParam Integer userId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(userService.activateUser(userId, user), HttpStatus.OK);
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivateUser(@RequestParam Integer userId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(userService.deactivateUser(userId, user), HttpStatus.OK);
    }

}
