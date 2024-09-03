package ru.puchinets.controller.rest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.puchinets.model.dto.request.LoginRequest;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    @PostMapping
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult.hasErrors()");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            request.login(loginDto.getUsername(), loginDto.getPassword());
        } catch (ServletException e) {
            System.out.println("ServletException");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
