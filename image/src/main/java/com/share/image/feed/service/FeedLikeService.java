package com.share.image.feed.service;

import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.FeedLike;
import com.share.image.feed.repository.FeedLikeRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;


    public void createFeedLike(User user, Feed feed){
        feedLikeRepository.save(FeedLike.create(user, feed));
    }


    public void deleteFeedLike(User user, Feed feed){
        FeedLike feedLike = feedLikeRepository.findByUserAndFeed(user, feed).orElseThrow(
                () -> new GlobalException(ErrorCode.FEED_LIKE_ERROR)
        );

        feed.getFeedLikes().remove(feedLike);

        feedLikeRepository.delete(feedLike);
    }


    @Transactional(readOnly = true)
    public boolean isUserLikeFeed(User user, Feed feed){
        return feedLikeRepository.existsByUserAndFeed(user ,feed);
    }
}
