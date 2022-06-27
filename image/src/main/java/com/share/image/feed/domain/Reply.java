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
public class Reply {

    @Id @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String replyLikeStatus;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL)
    private List<ReplyLike> replyLikes = new ArrayList<>();


    @Builder
    public Reply(Long id, String content, String replyLikeStatus, User writer, Feed feed){
        this.id = id;
        this.content = content;
        this.replyLikeStatus = replyLikeStatus;
        this.writer = writer;
        this.feed = feed;
        this.createdDate = LocalDateTime.now();
    }


    public void setReplyLikeStatus(String replyLikeStatus){
        this.replyLikeStatus = replyLikeStatus;
    }

}
