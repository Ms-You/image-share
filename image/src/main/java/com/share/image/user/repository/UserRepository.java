package com.share.image.user.repository;

import com.share.image.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickName(String nickName);
    Page<User> findByNickNameContaining(String keyword, Pageable pageable);
    Optional<User> findByEmail(String email);
    Boolean existsByNickName(String nickName);
    Boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findAllByTemporaryLocked(Pageable pageable, String temporaryLocked);

    List<User> findAllByTemporaryLocked(String temporaryLocked);

    Page<User> findAllByPermanentLocked(Pageable pageable, String permanentLocked);
}
