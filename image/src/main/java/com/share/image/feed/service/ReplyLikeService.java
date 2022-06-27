package com.share.image.feed.service;

import com.share.image.feed.domain.Reply;
import com.share.image.feed.repository.ReplyLikeRepository;
import com.share.image.feed.repository.ReplyRepository;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyLikeService {


    private final ReplyLikeRepository replyLikeRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;


    @Transactional(readOnly = true)
    public Boolean isUserLikeReply(Long replyId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Reply reply = replyRepository.findById(replyId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 댓글입니다.");
        });


        if (replyLikeRepository.findReplyLikeByReplyAndUser(reply.getId(), user.getId()) == null)
            return false;
        else
            return true;

    }

    public void likeReply(Long replyId, Long userId) {
        replyLikeRepository.likeReply(replyId, userId);
    }

    public void unLikeReply(Long replyId, Long userId) {
        replyLikeRepository.unLikeReply(replyId, userId);
    }
}
