package com.share.image.feed.service;

import com.share.image.feed.domain.Subscribe;
import com.share.image.feed.repository.SubscribeRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    // 구독 여부 확인
    @Transactional(readOnly = true)
    public Boolean isUserSubscribe(User toUser, User fromUser) {
        return subscribeRepository.findSubscribeByUserId(fromUser.getId(), toUser.getId()) == null ? false : true;
    }

    public void subscribe(Long fromUserId, Long toUserId) {
        subscribeRepository.subscribeUser(fromUserId, toUserId);
    }

    public void unSubscribe(Long fromUserId, Long toUserId) {
        subscribeRepository.unSubscribeUser(fromUserId, toUserId);
    }

    public Page<Subscribe> findSubscribeList(User user, Pageable pageable){
        return subscribeRepository.findByFromUserId(user.getId(), pageable);
    }

}
