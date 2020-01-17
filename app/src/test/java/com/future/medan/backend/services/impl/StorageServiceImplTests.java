package com.future.medan.backend.services.impl;

import com.future.medan.backend.services.StorageService;
import com.future.medan.backend.storage.StorageProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Path;

public class StorageServiceImplTests {

    @Mock
    private StorageProperties storageProperties;

    private StorageService storageService;

    private Path rootLocationBook, rootLocationImage;

    private String fileStr, fileName;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.fileStr = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==";
        this.fileName = "test";

    }

    @Test
    public void testStorePdf() throws IOException {

    }
}
