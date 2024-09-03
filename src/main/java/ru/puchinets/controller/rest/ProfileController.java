package ru.puchinets.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.puchinets.model.dto.request.ProfileRequest;
import ru.puchinets.model.dto.response.ProfileResponse;
import ru.puchinets.service.ProfileService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}/profiles")
    ResponseEntity<ProfileResponse> getProfileById(@PathVariable Long userId) {
        return profileService
                .getById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profiles")
    ResponseEntity<Page<ProfileResponse>> getAllProfiles(@PageableDefault(sort = "id", direction = Sort.Direction.ASC)
                                                         Pageable pageable) {
        return ResponseEntity.ok(profileService.getAll(pageable));
    }

    @PutMapping("{userId}/profiles")
    ResponseEntity<ProfileResponse> createUpdateProfile(@PathVariable Long userId, @RequestBody ProfileRequest request) {
        return profileService
                .createUpdate(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{userId}/profiles")
    ResponseEntity<Void> deleteProfile(@PathVariable Long userId) {
        boolean isDeleted = profileService.delete(userId);
        if (isDeleted) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

}
