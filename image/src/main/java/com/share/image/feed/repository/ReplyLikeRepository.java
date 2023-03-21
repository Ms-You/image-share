package com.share.image.feed.repository;

import com.share.image.feed.domain.Reply;
import com.share.image.feed.domain.ReplyLike;
import com.share.image.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {

    boolean existsByUserAndReply(User user, Reply reply);

    Optional<ReplyLike> findByUserAndReply(User user, Reply reply);

}
