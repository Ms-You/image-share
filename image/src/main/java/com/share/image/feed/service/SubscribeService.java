package com.share.image.feed.service;

import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Subscribe;
import com.share.image.feed.repository.SubscribeRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;


    public void createSubscribe(User fromUser, User toUser){
        subscribeRepository.save(Subscribe.create(fromUser, toUser));
    }

    public void deleteSubscribe(User fromUser, User toUser){
        Subscribe subscribe = subscribeRepository.findByFromUserAndToUser(fromUser, toUser).orElseThrow(
                () -> new GlobalException(ErrorCode.SUBSCRIBE_ERROR)
        );

        fromUser.getSubscribes().remove(subscribe);

        subscribeRepository.delete(subscribe);
    }

    @Transactional(readOnly = true)
    public boolean isSubscribe(User fromUser, User toUser){
        return subscribeRepository.existsByFromUserAndToUser(fromUser, toUser);
    }


    public Page<Subscribe> subscribePage(User fromUser, Pageable pageable){
        return subscribeRepository.findByFromUser(fromUser, pageable);
    }


}
