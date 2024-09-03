package ru.puchinets.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.puchinets.service.ProfileService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ImageController {

    private final ProfileService profileService;

    @GetMapping(value = "/{userId}/images")
    public ResponseEntity<byte[]> getImageByUserId(@PathVariable Long userId) {
        return profileService
                .getImage(userId)
                .map(content ->
                    ResponseEntity
                            .ok()
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                            .body(content)
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/images")
    public ResponseEntity<Void> loadNewImageByUserId(@PathVariable Long userId,
                                                         @RequestParam("image") MultipartFile image) {
        boolean imageUploaded = profileService.saveImageByUserId(userId, image);
        if (imageUploaded) return new ResponseEntity<>(HttpStatus.OK);
        else return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{userId}/images")
    public ResponseEntity<Void> deleteImageByUserId(@PathVariable Long userId) {
        boolean isImageRemoved = profileService.removeImageByUserId(userId);
        if (isImageRemoved) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

}
