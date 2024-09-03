package ru.puchinets.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.puchinets.model.dto.request.UserRequest;
import ru.puchinets.model.dto.response.Response;
import ru.puchinets.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<Response>> getAllUsers(@PageableDefault(sort = "id", direction = Sort.Direction.ASC)
                                                              Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> getById(@PathVariable Long userId) {
        return userService.getById(userId)
                .map(org.springframework.http.ResponseEntity::ok)
                .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Response> createUser(@RequestBody UserRequest request) {
        Response response = userService.createUser(request);
        if (response!=null) return new ResponseEntity<>(response, HttpStatus.CREATED);
        else return org.springframework.http.ResponseEntity.badRequest().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Response> updateUser(@RequestBody UserRequest request,
                                                                        @PathVariable Long userId) {
        return userService
                .updateUser(userId, request)
                .map(org.springframework.http.ResponseEntity::ok)
                .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }
}
