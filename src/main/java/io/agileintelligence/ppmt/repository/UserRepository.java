package io.agileintelligence.ppmt.repository;

import io.agileintelligence.ppmt.domain.UserBO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserBO, Long> {

    UserBO findByUsername(String username);

    UserBO getById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}