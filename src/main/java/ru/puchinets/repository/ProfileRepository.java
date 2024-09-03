package ru.puchinets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.puchinets.model.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
