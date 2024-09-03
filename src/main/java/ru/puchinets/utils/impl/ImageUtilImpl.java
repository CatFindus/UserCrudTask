package ru.puchinets.utils.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.puchinets.utils.ImageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

/*
Будет использовано сохранение картинок на жесткий диск с хранением в БД ссылок на картинки.
Т.к. хранить в БД картинки не лучшее решение, а внешнюю интерграцию на текущем этапе делать трудозатратно
*/
@Getter
@Service
public class ImageUtilImpl implements ImageUtil {

    @Value(value = "${app.image.bucket}")
    private String bucket;

    public String upload(String imagePath, InputStream content, Long userId) throws IOException {
        Path fullImagePath = Path.of(bucket, userId.toString(), imagePath);
        try (content) {
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        return fullImagePath.toString();
    }

    @Override
    public boolean remove(String location) throws IOException {
        Path path = Path.of(location);
        return Files.deleteIfExists(path);

    }

    @Override
    public Optional<byte[]> get(String location) throws IOException {
        Path path = Path.of(location);
        return Files.exists(path) ? Optional.of(Files.readAllBytes(path)) : Optional.empty();
    }
}
