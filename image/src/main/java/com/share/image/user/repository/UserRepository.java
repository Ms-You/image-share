package com.share.image.user.repository;

import com.share.image.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickName(String nickName);

    Optional<User> findByEmail(String email);
    Boolean existsByNickName(String nickName);
    Boolean existsByEmail(String email);
}
