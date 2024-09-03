package ru.puchinets.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.puchinets.mapper.ProfileMapper;
import ru.puchinets.model.dto.request.ProfileRequest;
import ru.puchinets.model.dto.response.ProfileResponse;
import ru.puchinets.model.entity.Profile;
import ru.puchinets.model.entity.User;
import ru.puchinets.repository.ProfileRepository;
import ru.puchinets.repository.UserRepository;
import ru.puchinets.service.ProfileService;
import ru.puchinets.utils.ImageUtil;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ImageUtil imageUtil;

    @Override
    public Optional<ProfileResponse> getById(Long userId) {
        return profileRepository
                .findById(userId)
                .map(profileMapper::entityToDto);
    }

    @Override
    public Page<ProfileResponse> getAll(Pageable pageable) {
        return profileRepository
                .findAll(pageable)
                .map(profileMapper::entityToDto);
    }

    @Transactional
    @Override
    public Optional<ProfileResponse> createUpdate(Long userId, ProfileRequest request) {
        if (request == null || !userRepository.existsById(userId)) return Optional.empty();
        boolean profileIsExists = profileRepository.existsById(userId);
        if (!profileIsExists) {
            Profile newProfile = profileMapper.dtoToEntity(request);
            newProfile.setId(userId);
            newProfile = profileRepository.save(newProfile);
            return Optional.of(profileMapper.entityToDto(newProfile));
        } else return profileRepository
                .findById(userId)
                .map(entity -> profileMapper.updateFields(entity, request))
                .map(profileRepository::saveAndFlush)
                .map(profileMapper::entityToDto);
    }

    @Transactional
    @Override
    public boolean delete(Long userId) {
        Optional<User> mayBeUser = userRepository.findById(userId);
        if (mayBeUser.isEmpty()) return false;
        mayBeUser.get().setProfile(null);
        userRepository.saveAndFlush(mayBeUser.get());
        profileRepository.deleteById(userId);
        return true;
    }

    @Transactional
    @Override
    public boolean saveImageByUserId(Long userId, MultipartFile image) {
        Optional<User> mayBeUser = userRepository.findById(userId);
        if (mayBeUser.isEmpty() || image == null) return false;
        String path;
        try {
            Profile profile = mayBeUser.get().getProfile();
            path = imageUtil.upload(image.getOriginalFilename(), image.getInputStream(), userId);
            if (profile != null) {
                if (profile.getPhoto()!=null) imageUtil.remove(profile.getPhoto());
                profile.setPhoto(path);
            } else {
                profile = Profile
                        .builder()
                        .id(userId)
                        .photo(path)
                        .build();
            }
            profileRepository.saveAndFlush(profile);
        } catch (IOException e) {
            //todo: handling exception
            return false;
        }
        return true;
    }

    @Override
    public Optional<byte[]> getImage(Long userId) {
        Optional<Profile> mayBeProfile = profileRepository.findById(userId);
        if (mayBeProfile.isPresent()) {
            String imageLocation = mayBeProfile.get().getPhoto();
            try {
                return imageUtil.get(imageLocation);
            } catch (IOException e) {
                //todo: handling exception
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public boolean removeImageByUserId(Long userId) {
        Optional<Profile> mayBeProfile = profileRepository.findById(userId);
        if (mayBeProfile.isPresent()) {
            String imageLocation = mayBeProfile.get().getPhoto();
            try {
                Profile profile = mayBeProfile.get();
                profile.setPhoto(null);
                profileRepository.saveAndFlush(profile);
                return imageUtil.remove(imageLocation);
            } catch (IOException e) {
                //todo: handling exception
                return false;
            }
        } else return false;
    }
}
