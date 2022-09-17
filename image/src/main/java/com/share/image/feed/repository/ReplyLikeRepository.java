package com.share.image.feed.repository;

import com.share.image.feed.domain.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {

    @Query(value = "select reply_like_id from reply_like where reply_id = :replyId and user_id = :userId", nativeQuery = true)
    Long findReplyLikeByReplyAndUser(@Param(value = "replyId") Long replyId, @Param(value = "userId") Long userId);

    @Modifying
    @Query(value = "insert into reply_like(reply_id, user_id) values(:replyId, :userId)", nativeQuery = true)
    void likeReply(@Param(value = "replyId") Long replyId, @Param(value = "userId") Long userId);

    @Modifying
    @Query(value = "delete from reply_like where reply_id = :replyId and user_id = :userId", nativeQuery = true)
    void unLikeReply(@Param(value = "replyId") Long replyId, @Param(value = "userId") Long userId);


}
