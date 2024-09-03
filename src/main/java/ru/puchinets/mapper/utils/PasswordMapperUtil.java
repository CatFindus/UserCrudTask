package ru.puchinets.mapper.utils;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Named("PasswordMapperUtil")
@Component
@RequiredArgsConstructor
public class PasswordMapperUtil {

    private final PasswordEncoder passwordEncoder;

    @Named("getPasswordHash")
    public String getPasswordHash(String password) {
        return passwordEncoder.encode(password);
    }
}
