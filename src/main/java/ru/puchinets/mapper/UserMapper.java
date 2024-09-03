package ru.puchinets.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.puchinets.mapper.utils.PasswordMapperUtil;
import ru.puchinets.model.dto.request.UserRequest;
import ru.puchinets.model.dto.response.UserResponse;
import ru.puchinets.model.entity.User;

@Mapper(componentModel = "spring",uses = {PasswordMapperUtil.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = {"PasswordMapperUtil", "getPasswordHash"})
    User dtoToEntity(UserRequest request);

    UserResponse entityToDto(User entity);

    default User updateFields(User user, UserRequest request) {
        if (request == null) return user;
        if (request.getPassword() != null && !request.getPassword().isBlank())
            user.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
        return user;
    }

}
