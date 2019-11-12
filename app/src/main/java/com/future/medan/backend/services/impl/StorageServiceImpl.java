package com.future.medan.backend.services.impl;

import com.future.medan.backend.services.StorageService;
import com.future.medan.backend.storage.Base64DecodedMultipartFile;
import com.future.medan.backend.storage.StorageException;
import com.future.medan.backend.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String storePdf(String fileStr, String fileName) throws IOException {
        Base64DecodedMultipartFile file;

        byte[] decodedString = Base64.getDecoder().decode(fileStr);
        file = new Base64DecodedMultipartFile(decodedString);
        String filename = StringUtils.cleanPath(fileName + ".pdf");
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
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return filename;
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

}
