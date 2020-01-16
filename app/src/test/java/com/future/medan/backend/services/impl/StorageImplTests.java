package com.future.medan.backend.services.impl;

import com.future.medan.backend.storage.StorageProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageImplTests {

    @Mock
    private StorageProperties storageProperties;

    private Path rootLocationBook, rootLocationImage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.rootLocationBook = Paths.get(storageProperties.getLocationBook());
        this.rootLocationImage = Paths.get(storageProperties.getLocationImage());
    }

    @Test
    public void testStorage() {

    }
}
