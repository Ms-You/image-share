package com.share.image.feed.service;

import com.share.image.feed.domain.Reply;
import com.share.image.feed.repository.ReplyLikeRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyLikeService {

    private final ReplyLikeRepository replyLikeRepository;

    @Transactional(readOnly = true)
    public Boolean isUserLikeReply(User user, Reply reply) {
        return replyLikeRepository.findReplyLikeByReplyAndUser(reply.getId(), user.getId()) == null ? false : true;
    }

    public void likeReply(User user, Reply reply) {
        replyLikeRepository.likeReply(reply.getId(), user.getId());
    }

    public void unLikeReply(User user, Reply reply) {
        replyLikeRepository.unLikeReply(reply.getId(), user.getId());
    }
}
