package com.share.image.feed.service;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedLikeRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FeedLikeService {

    private final FeedLikeRepository likeRepository;

    @Transactional(readOnly = true)
    public Boolean isUserLikeFeed(User user, Feed feed) {
        return likeRepository.findLikeByFeedAndUser(feed.getId(), user.getId()) == null ? false : true;
    }


    public void likeFeed(User user, Feed feed) {
        likeRepository.likeFeed(feed.getId(), user.getId());
    }


    public void unLikeFeed(User user, Feed feed) {
        likeRepository.unLikeFeed(feed.getId(), user.getId());
    }




}
