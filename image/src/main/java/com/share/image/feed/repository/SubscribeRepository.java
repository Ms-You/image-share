package com.share.image.feed.repository;

import com.share.image.feed.domain.Subscribe;
import com.share.image.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    Optional<Subscribe> findByFromUserAndToUser(User fromUser, User toUser);

    Page<Subscribe> findByFromUser(User fromUser, Pageable pageable);
}
