package com.future.medan.backend.services;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    void init();

    String storePdf(String file, String fileName) throws IOException;

    String storeImage(String file, String fileName) throws  IOException;

    String loadBook(String fileName) throws IOException;

    String loadImage(String fileName) throws IOException;
}
