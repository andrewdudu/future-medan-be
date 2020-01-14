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

    private Sequence sequence, sequenceNew;
    private String key, key2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.sequenceService = new SequenceServiceImpl(sequenceRepository);

        this.sequence = new Sequence();
        this.sequenceNew = null;
        this.key = "SU";
        this.key2 = "NU";

        sequence.setKey(key);
        sequence.setValue(11);
    }

    @Test
    public void save_Ok() {
        String expected = "SU-0012";
        when(sequenceRepository.findByKey(key)).thenReturn(sequence);
        when(sequenceRepository.save(sequence)).thenReturn(sequence);

        assertEquals(expected, sequenceService.save(key));
    }

    @Test
    public void save_SequenceNull() {
        String expected = "NU-0001";
        when(sequenceRepository.findByKey(key2)).thenReturn(sequenceNew);

        sequenceNew = new Sequence();
        sequenceNew.setKey(key2);
        sequenceNew.setValue(0);
        when(sequenceRepository.save(sequenceNew)).thenReturn(sequenceNew);

        String actual = sequenceService.save(key2);
        assertEquals(expected, actual);
    }
}
