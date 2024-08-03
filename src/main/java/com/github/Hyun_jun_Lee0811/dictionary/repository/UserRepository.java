package com.github.Hyun_jun_Lee0811.dictionary.repository;

import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  boolean existsByUsername(String username);

}
