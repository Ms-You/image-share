package com.share.image.feed.repository;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findByTag(Tag tag, Pageable pageable);

    @Query(value = "select feed_id from Feed where feed_id = (select MIN(feed_id) from Feed where feed_id > :feedId and tag_id = :tagId)", nativeQuery = true)
    Long lagFeedId(@Param(value = "feedId") Long feedId, @Param(value = "tagId") Long tagId);

    @Query(value = "select feed_id from Feed where feed_id = (select MAX(feed_id) from Feed where feed_id < :feedId and tag_id = :tagId)", nativeQuery = true)
    Long leadFeedId(@Param(value = "feedId") Long feedId, @Param(value = "tagId") Long tagId);

    Page<Feed> findByTitleContaining(String keyword, Pageable pageable);
    @Query(value = "select feed_id from Views group by feed_id order by count(feed_id) desc, feed_id desc limit 5 offset :offset", nativeQuery = true)
    List<Long> findFeedIdByViewsDesc(@Param(value = "offset") int offset);

    @Query(value = "select feed_id from Feed_like group by feed_id order by count(feed_id) desc, feed_id desc limit 5 offset :offset", nativeQuery = true)
    List<Long> findFeedIdByLikesDesc(@Param(value = "offset") int offset);

    @Query(value = "select feed_id from Feed order by createdDate desc limit 5 offset :offset", nativeQuery = true)
    List<Long> findFeedIdByCreatedDateDesc(@Param(value = "offset") int offset);

    @Query(value = "select feed_id from Feed_like where user_id = :userId group by feed_id order by count(feed_id) desc, feed_id desc limit 5 offset :offset", nativeQuery = true)
    List<Long> findFeedIdByUserIdAndLikesDesc(@Param(value = "userId") Long userId, @Param(value = "offset") int offset);

    @Query(value = "select user_id from Feed where feed_id = :feedId", nativeQuery = true)
    Long findUserIdByFeedId(@Param(value = "feedId") Long feedId);
}
