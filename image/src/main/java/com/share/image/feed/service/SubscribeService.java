package com.share.image.feed.service;

import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.SubscribeRepository;
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
public class SubscribeService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final SubscribeRepository subscribeRepository;

    // 구독 여부 확인
    @Transactional(readOnly = true)
    public Boolean isUserSubscribe(Long feedId, Long userId) {
        User fromUser = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Long toUserId = feedRepository.findUserIdByFeedId(feedId);

        if (subscribeRepository.findSubscribeByUserId(fromUser.getId(), toUserId) == null)	// 구독중이 아님
            return false;
        else
            return true;

    }

    public void subscribe(Long fromUserId, Long feedId) {
        Long toUserId = feedRepository.findUserIdByFeedId(feedId);
        subscribeRepository.subscribeUser(fromUserId, toUserId);
    }

    public void unSubscribe(Long fromUserId, Long feedId) {
        Long toUserId = feedRepository.findUserIdByFeedId(feedId);
        subscribeRepository.unSubscribeUser(fromUserId, toUserId);
    }

}
