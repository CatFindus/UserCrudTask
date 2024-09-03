package ru.puchinets.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface ImageUtil {
    String upload(String imagePath, InputStream content, Long userId) throws IOException;
    boolean remove(String location) throws IOException;
    Optional<byte[]> get(String location) throws IOException;

}
