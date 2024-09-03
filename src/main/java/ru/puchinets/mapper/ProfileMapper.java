package ru.puchinets.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.puchinets.model.dto.request.ProfileRequest;
import ru.puchinets.model.dto.response.ProfileResponse;
import ru.puchinets.model.entity.Profile;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {

    Profile dtoToEntity(ProfileRequest request);

    ProfileResponse entityToDto(Profile entity);

    default Profile updateFields(Profile entity, ProfileRequest request) {
        if (request==null) return entity;
        if (request.getEmail()!=null && !request.getEmail().isBlank())
            entity.setEmail(request.getEmail());
        if (request.getFirstname() != null && !request.getFirstname().isBlank())
            entity.setFirstname(request.getFirstname());
        if (request.getMiddleName() != null && !request.getMiddleName().isBlank())
            entity.setMiddleName(request.getMiddleName());
        if (request.getLastname() != null && !request.getLastname().isBlank())
            entity.setLastname(request.getLastname());
        if (request.getBirthDate() != null) entity.setBirthDate(request.getBirthDate());
        if (request.getPhone() != null && !request.getPhone().isBlank())
            entity.setPhone(request.getPhone());
        return entity;
    }
}
