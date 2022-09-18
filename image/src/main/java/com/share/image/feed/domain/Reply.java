package com.share.image.feed.domain;

import com.share.image.user.domain.BaseTimeEntity;
import com.share.image.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Reply extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String replyLikeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL)
    private List<ReplyLike> replyLikes = new ArrayList<>();


    //== 생성 메서드 ==//
    public static Reply createReply(String content, String replyLikeStatus, User writer, Feed feed) {
        Reply reply = new Reply();
        reply.writer = writer;
        reply.feed = feed;
        reply.replyLikeStatus = replyLikeStatus;
        reply.updateReply(content);

        writer.getReplies().add(reply);
        feed.getReplies().add(reply);

        return reply;
    }

    public void updateReply(String content){
        this.content = content;
    }

    public void updateReplyLikeStatus(String replyLikeStatus){
        this.replyLikeStatus = replyLikeStatus;
    }

}
