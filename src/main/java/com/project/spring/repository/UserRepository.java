package com.project.spring.repository;

import com.project.spring.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {

    Optional<UserInfo> findByUsername(String username);

    UserInfo findByVerificationToken(String verificationToken);

    List<UserInfo> findByIsVerifiedFalseAndExpirationTimeBefore(LocalDateTime time);

}

