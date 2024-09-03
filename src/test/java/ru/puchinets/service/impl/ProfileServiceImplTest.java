package ru.puchinets.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.puchinets.mapper.ProfileMapper;
import ru.puchinets.model.dto.request.ProfileRequest;
import ru.puchinets.model.dto.response.ProfileResponse;
import ru.puchinets.model.entity.Profile;
import ru.puchinets.repository.ProfileRepository;
import ru.puchinets.repository.UserRepository;
import ru.puchinets.utils.ImageUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    private static final Long PROFILE_ID_NOT_EMPTY = 1L;
    private static final Long PROFILE_ID_EMPTY = 2L;
    private Profile profile = Profile.builder().id(PROFILE_ID_NOT_EMPTY).build();
    private ProfileResponse response = ProfileResponse.builder().id(PROFILE_ID_NOT_EMPTY).build();
    private ProfileRequest request = new ProfileRequest("test@mail.ru", "test_fn", "test_mn", "test_ln", LocalDate.now(), "+79111311117");

    @Mock
    private ProfileMapper profileMapper;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageUtil imageUtil;
    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void getById() {
        doReturn(Optional.of(profile)).when(profileRepository).findById(PROFILE_ID_NOT_EMPTY);
        doReturn(response).when(profileMapper).entityToDto(profile);
        var actual = profileService.getById(PROFILE_ID_NOT_EMPTY);
        assertTrue(actual.isPresent());
        assertEquals(response, actual.get());
        doReturn(Optional.empty()).when(profileRepository).findById(PROFILE_ID_EMPTY);
        var emptyActual = profileService.getById(PROFILE_ID_EMPTY);
        assertFalse(emptyActual.isPresent());
    }

    @Test
    void getAll() {
        Page<Profile> profilePage = new PageImpl<>(List.of(profile), Pageable.unpaged(), 1L);
        doReturn(profilePage).when(profileRepository).findAll(Pageable.unpaged());
        doReturn(response).when(profileMapper).entityToDto(profile);
        var actual = profileService.getAll(Pageable.unpaged());
        assertTrue(actual.hasContent());
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(response, actual.getContent().get(0));
    }

    @Test
    void createUpdate() {
    }

    @Test
    void delete() {
    }

    @Test
    void saveImageByUserId() {
    }

    @Test
    void getImage() {
    }

    @Test
    void removeImageByUserId() {
    }
}