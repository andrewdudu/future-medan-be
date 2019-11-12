package com.future.medan.backend.services;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    String storePdf(String file, String fileName) throws IOException;

    Path load(String filename);
}
