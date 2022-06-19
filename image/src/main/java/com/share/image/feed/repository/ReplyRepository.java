package com.share.image.feed.repository;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByFeed(Feed feed);
}
