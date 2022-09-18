package com.share.image.feed.service;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.ViewRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ViewService {

    private final ViewRepository viewRepository;

    @Transactional(readOnly = true)
    public Boolean isUserViewFeed(User user, Feed feed) {
        return viewRepository.findViewsByFeedAndUser(feed.getId(), user.getId()) == null ? true : false;
    }


    public void viewFeed(Long feedId, Long userId){
        viewRepository.viewFeed(feedId, userId);
    }


}
