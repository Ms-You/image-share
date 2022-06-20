package com.share.image.feed.service;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.dto.ReplyRequestDto;
import com.share.image.feed.repository.ReplyRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }


    public void createReply(User user, ReplyRequestDto replyRequestDto, Feed feed) {
        Reply reply = Reply.builder()
                .content(replyRequestDto.getContent())
                .writer(user)
                .feed(feed)
                .build();

        replyRepository.save(reply);

    }


    public Reply findByReplyId(Long replyId){
        return replyRepository.findById(replyId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 댓글입니다.");
        });
    }

    public void deleteReply(Reply reply){
        replyRepository.delete(reply);
    }


}
