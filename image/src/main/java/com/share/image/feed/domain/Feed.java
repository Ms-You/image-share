package com.share.image.feed.domain;

import com.share.image.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Feed {

    @Id @GeneratedValue
    @Column(name = "feed_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String feedImageUrl;

    @CreatedDate
    private LocalDateTime createdDate;

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
    private List<View> views = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Feed(Long id, String title, String description, Tag tag, User writer){
        this.id = id;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.writer = writer;
        this.createdDate = LocalDateTime.now();
    }

    public void modifyFeed(String title, String description, Tag tag){
        this.title = title;
        this.description = description;
        this.tag = tag;
    }


    public void updateFeedImageUrl(String feedImageUrl){
        this.feedImageUrl = feedImageUrl;
    }

}
