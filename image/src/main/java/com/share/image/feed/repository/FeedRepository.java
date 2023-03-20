package com.share.image.feed.repository;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import com.share.image.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findByTag(Tag tag, Pageable pageable);

    @Query(value = "select f.id from Feed f where f.id = (select MIN(f.id) from Feed f where f.id > :feedId and f.tag.id = :tagId)")
    Long lagFeedId(@Param(value = "feedId") Long feedId, @Param(value = "tagId") Long tagId);

    @Query(value = "select f.id from Feed f where f.id = (select MAX(f.id) from Feed f where f.id < :feedId and f.tag.id = :tagId)")
    Long leadFeedId(@Param(value = "feedId") Long feedId, @Param(value = "tagId") Long tagId);

    @Query(value = "select f.id from Feed f where f.id = (select MIN(f.id) from Feed f where f.id > :feedId and f.writer.id = :userId)")
    Long toUsersLagFeedId(@Param(value = "feedId") Long feedId, @Param(value = "userId") Long userId);

    @Query(value = "select f.id from Feed f where f.id = (select MAX(f.id) from Feed f where f.id < :feedId and f.writer.id = :userId)")
    Long toUsersLeadFeedId(@Param(value = "feedId") Long feedId, @Param(value = "userId") Long userId);

    Page<Feed> findByTitleContaining(String keyword, Pageable pageable);

    Page<Feed> findByWriter(User writer, Pageable pageable);
}
