package com.share.image.feed.repository;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.FeedLike;
import com.share.image.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    boolean existsByUserAndFeed(User user, Feed feed);

    Optional<FeedLike> findByUserAndFeed(User user, Feed feed);

}
