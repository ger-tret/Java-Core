package com.user.service.repository;

import com.user.service.model.entity.CardMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<CardMetadata, Long> {
    @Query(value = "SELECT c.* FROM card_info c JOIN users u ON c.user_id = u.id WHERE u.email = :email", nativeQuery = true)
    List<CardMetadata> findCardsByUserEmail(@Param("email") String email);
}
