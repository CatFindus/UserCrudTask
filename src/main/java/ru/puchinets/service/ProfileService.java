package ru.puchinets.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.puchinets.model.dto.request.ProfileRequest;
import ru.puchinets.model.dto.response.ProfileResponse;

import java.util.Optional;

public interface ProfileService {

    Optional<ProfileResponse> getById(Long userId);

    Page<ProfileResponse> getAll(Pageable pageable);

    Optional<ProfileResponse> createUpdate(Long userId, ProfileRequest request);

    boolean delete(Long userId);

    boolean saveImageByUserId(Long userId, MultipartFile image);

    Optional<byte[]> getImage(Long userId);

    boolean removeImageByUserId(Long userId);
}
