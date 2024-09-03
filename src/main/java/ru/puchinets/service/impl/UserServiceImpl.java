package ru.puchinets.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.puchinets.mapper.UserMapper;
import ru.puchinets.model.dto.request.UserRequest;
import ru.puchinets.model.dto.response.ProfileResponse;
import ru.puchinets.model.dto.response.UserResponse;
import ru.puchinets.model.dto.response.Response;
import ru.puchinets.model.entity.User;
import ru.puchinets.repository.UserRepository;
import ru.puchinets.service.ProfileService;
import ru.puchinets.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileService profileService;


    @Override
    public Optional<Response> getById(Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::entityToDto);
    }

    @Override
    public Page<Response> getAllUsers(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(userMapper::entityToDto);
    }

    @Transactional
    @Override
    public Response createUser(UserRequest request) {
        if (request==null) return null;
        User newUser = userMapper.dtoToEntity(request);
        newUser = userRepository.saveAndFlush(newUser);
        UserResponse response = userMapper.entityToDto(newUser);
        if (request.getProfile()!=null) {
            Optional<ProfileResponse> mayBeNewProfile = profileService.createUpdate(newUser.getId(), request.getProfile());
            mayBeNewProfile.ifPresent(response::setProfile);
        }
        return response;
    }

    @Transactional
    @Override
    public Optional<Response> updateUser(Long userId, UserRequest request) {
        Optional<User> mayBeUser = userRepository.findById(userId);
        if (mayBeUser.isEmpty()) return Optional.empty();
        if (request==null) return mayBeUser.map(userMapper::entityToDto);
        User user = mayBeUser.get();
        user = userRepository.saveAndFlush(userMapper.updateFields(user, request));
        UserResponse response = userMapper.entityToDto(user);
        if (request.getProfile()!=null) {
            Optional<ProfileResponse> mayBeProfile = profileService.createUpdate(userId, request.getProfile());
            mayBeProfile.ifPresent(response::setProfile);
        }
        return Optional.of(response);
    }

    @Transactional
    @Override
    public boolean deleteUser(Long userId) {
        boolean isExistById = userRepository.existsById(userId);
        if (isExistById) userRepository.deleteById(userId);
        return isExistById;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
