package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(NotificationController.API_PATH)
public class NotificationController {

    public static final String API_PATH = "/api/notification";

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean unreadOnly,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(notificationService.getNotifications(user, unreadOnly, page, size), HttpStatus.OK);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(notificationService.countUnread(user), HttpStatus.OK);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(notificationService.markAsRead(notificationId, user), HttpStatus.OK);
    }

    @PatchMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(notificationService.markAllAsRead(user), HttpStatus.OK);
    }

}
