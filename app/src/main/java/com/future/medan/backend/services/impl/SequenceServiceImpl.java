package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Sequence;
import com.future.medan.backend.repositories.SequenceRepository;
import com.future.medan.backend.services.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceServiceImpl implements SequenceService {

    SequenceRepository sequenceRepository;

    @Autowired
    public SequenceServiceImpl(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public String save(String key) {
        Sequence sequence = sequenceRepository.findByKey(key);

        if (sequence == null)
            sequence = new Sequence(key, 0);

        sequence.setValue(sequence.getValue() + 1);
        sequence = sequenceRepository.save(sequence);

        return key + '-' + String.format("%04d", sequence.getValue());
    }
}
