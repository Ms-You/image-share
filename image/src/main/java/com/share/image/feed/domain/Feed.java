package com.share.image.feed.domain;

import com.share.image.feed.dto.FeedRequestDto;
import com.share.image.user.domain.BaseTimeEntity;
import com.share.image.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Feed extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "feed_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String feedImageUrl;

    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<FeedLike> feedLikes = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();


    //== 생성 메서드 ==//
    public static Feed createFeed(User user, FeedRequestDto feedRequestDto, Tag tag){
        Feed feed = new Feed();

        feed.updateFeed(user, feedRequestDto.getTitle(), feedRequestDto.getDescription(), tag);
        user.getFeeds().add(feed);
        tag.getFeeds().add(feed);

        return feed;
    }

    public void updateFeed(User writer, String title, String description, Tag tag){
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.tag = tag;
    }

    public void updateFeedImageUrl(String feedImageUrl){
        this.feedImageUrl = feedImageUrl;
    }

    public void increaseView(){
        this.view++;
    }

}
