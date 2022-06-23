package com.share.image.feed.service;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.ViewRepository;
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
public class ViewService {

    private final ViewRepository viewRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    @Transactional(readOnly = true)
    public Boolean isUserViewFeed(Long feedId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        if (viewRepository.findViewsByFeedAndUser(feed.getId(), user.getId()) == null)	// 아직 안봄
            return true;

        return false;
    }


    public void viewFeed(Long feedId, Long userId){
        viewRepository.viewFeed(feedId, userId);
    }


}
