package com.share.image.feed.service;

import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.domain.ReplyLike;
import com.share.image.feed.repository.ReplyLikeRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReplyLikeService {

    private final ReplyLikeRepository replyLikeRepository;


    public void createReplyLike(User user, Reply reply){
        replyLikeRepository.save(ReplyLike.create(user, reply));
    }


    public void deleteReplyLike(User user, Reply reply){
        ReplyLike replyLike = replyLikeRepository.findByUserAndReply(user, reply).orElseThrow(
                () -> new GlobalException(ErrorCode.REPLY_LIKE_ERROR)
        );

        reply.getReplyLikes().remove(replyLike);

        replyLikeRepository.delete(replyLike);
    }


    @Transactional(readOnly = true)
    public boolean isUserLikeReply(User user, Reply reply){
        return replyLikeRepository.existsByUserAndReply(user, reply);
    }

}
