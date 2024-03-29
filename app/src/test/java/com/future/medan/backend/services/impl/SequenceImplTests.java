package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Sequence;
import com.future.medan.backend.repositories.SequenceRepository;
import com.future.medan.backend.services.SequenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SequenceImplTests {

    @Mock
    private SequenceRepository sequenceRepository;

    private SequenceService sequenceService;

    private Sequence sequence;
    private String key;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.sequenceService = new SequenceServiceImpl(sequenceRepository);

        this.sequence = new Sequence();
        this.key = "11";

        sequence.setKey(key);
        sequence.setValue(11);
    }

    @Test
    public void save_Ok() {
        String expected = "11-0012";
        when(sequenceRepository.findByKey(key)).thenReturn(sequence);
        when(sequenceRepository.save(sequence)).thenReturn(sequence);

        assertEquals(expected, sequenceService.save(key));
    }
}
