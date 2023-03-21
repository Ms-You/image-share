package com.share.image.feed.domain;

import com.share.image.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "reply_like")
public class ReplyLike {

    @Id
    @GeneratedValue
    @Column(name = "reply_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;


    public static ReplyLike create(User user, Reply reply){
        ReplyLike replyLike = new ReplyLike();
        replyLike.user = user;
        replyLike.reply = reply;

        reply.getReplyLikes().add(replyLike);

        return replyLike;
    }

}
