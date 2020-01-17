package com.future.medan.backend.services.impl;

import com.future.medan.backend.services.StorageService;
import com.future.medan.backend.storage.Base64DecodedMultipartFile;
import com.future.medan.backend.storage.FileNotFoundException;
import com.future.medan.backend.storage.StorageException;
import com.future.medan.backend.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocationBook;

    private final Path rootLocationImage;

    private StorageProperties storageProperties;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.storageProperties = properties;
        this.rootLocationBook = Paths.get(properties.getLocationBook());
        this.rootLocationImage = Paths.get(properties.getLocationImage());
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocationBook);
            Files.createDirectories(rootLocationImage);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String storePdf(String fileStr, String fileName) throws IOException {
        return store(fileStr, fileName + ".pdf", this.rootLocationBook);
    }

    @Override
    public String storeImage(String fileStr, String fileName) throws IOException {
        return "/api/get-image/" + store(fileStr, fileName + ".png", this.rootLocationImage);
    }

    private String store(String fileStr, String fileName, Path path) {
        Base64DecodedMultipartFile file;

        byte[] decodedString = Base64.getDecoder().decode(fileStr);
        file = new Base64DecodedMultipartFile(decodedString);
        String filename = StringUtils.cleanPath(fileName);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return filename;
    }

    @Override
    public byte[] loadImage(String fileName) throws IOException {
        File file = new File(this.storageProperties.getLocationImage() + "/" + fileName);

        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);

            return bytes;
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    @Override
    public byte[] loadBook(String fileName) throws IOException {
        File file = new File(this.storageProperties.getLocationBook() + "/" + fileName);

        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);

            return bytes;
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    private static String getFileToBase64(File file) throws IOException {
        String encodedFile = null;

        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedFile = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }

        return encodedFile;
    }
}
