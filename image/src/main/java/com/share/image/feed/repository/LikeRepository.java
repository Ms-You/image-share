package com.share.image.feed.repository;

import com.share.image.feed.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "select like_id from likes where feed_id = :feedId and user_id = :userId", nativeQuery = true)
    Long findLikesByFeedAndUser(Long feedId, Long userId);

    @Modifying
    @Query(value = "insert into likes(feed_id, user_id) values(:feedId, :userId)", nativeQuery = true)
    void likeFeed(Long feedId, Long userId);

    @Modifying
    @Query(value = "delete from likes where feed_id = :feedId and user_id = :userId", nativeQuery = true)
    void unLikeFeed(Long feedId, Long userId);


}
