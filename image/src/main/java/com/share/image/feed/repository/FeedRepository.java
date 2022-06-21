package com.share.image.feed.repository;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findByTag(Tag tag, Pageable pageable);

    @Query(value = "select feed_id from Feed where feed_id = (select MIN(feed_id) from Feed where feed_id > :feedId and tag_id = :tagId)", nativeQuery = true)
    Long lagFeedId(@Param(value = "feedId") Long feedId, @Param(value = "tagId") Long tagId);

    @Query(value = "select feed_id from Feed where feed_id = (select MAX(feed_id) from Feed where feed_id < :feedId and tag_id = :tagId)", nativeQuery = true)
    Long leadFeedId(@Param(value = "feedId") Long feedId, @Param(value = "tagId") Long tagId);

    Page<Feed> findByTitleContaining(String keyword, Pageable pageable);

    @Modifying
    @Query("update Feed f set f.view = f.view + 1 where f.id = :feedId")
    void updateView(Long feedId);

}
