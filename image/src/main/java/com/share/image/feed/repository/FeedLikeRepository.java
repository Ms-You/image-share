package com.share.image.feed.repository;

import com.share.image.feed.domain.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    @Query(value = "select feed_like_id from feed_like where feed_id = :feedId and user_id = :userId", nativeQuery = true)
    Long findLikeByFeedAndUser(Long feedId, Long userId);

    @Modifying
    @Query(value = "insert into feed_like(feed_id, user_id) values(:feedId, :userId)", nativeQuery = true)
    void likeFeed(Long feedId, Long userId);

    @Modifying
    @Query(value = "delete from feed_like where feed_id = :feedId and user_id = :userId", nativeQuery = true)
    void unLikeFeed(Long feedId, Long userId);


}
