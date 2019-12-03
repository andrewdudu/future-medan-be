package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceRepository extends JpaRepository<Sequence, String> {

    Sequence findByKey(String key);
}
