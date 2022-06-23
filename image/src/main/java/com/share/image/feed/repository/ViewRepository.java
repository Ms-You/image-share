package com.share.image.feed.repository;

import com.share.image.feed.domain.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ViewRepository extends JpaRepository<View, Long> {

    @Query(value = "select view_id from views where feed_id = :feedId and user_id = :userId", nativeQuery = true)
    Long findViewsByFeedAndUser(Long feedId, Long userId);

    @Modifying
    @Query(value = "insert into views(feed_id, user_id) values(:feedId, :userId)", nativeQuery = true)
    void viewFeed(Long feedId, Long userId);
}
