package com.share.image.feed.domain;

import com.share.image.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "feed_like")
public class FeedLike {

    @Id @GeneratedValue
    @Column(name = "feed_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;


    public static FeedLike create(User user, Feed feed){
        FeedLike feedLike = new FeedLike();
        feedLike.user = user;
        feedLike.feed = feed;

        return feedLike;
    }

}
