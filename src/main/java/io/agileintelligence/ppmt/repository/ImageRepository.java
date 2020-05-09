package io.agileintelligence.ppmt.repository;

import io.agileintelligence.ppmt.domain.ImageBO;
import io.agileintelligence.ppmt.domain.UserBO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageBO, Long> {

    ImageBO findByUserBO(Long id);
}
