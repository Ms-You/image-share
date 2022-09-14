package com.share.image.feed.domain;

import com.share.image.user.domain.BaseTimeEntity;
import com.share.image.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Report extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "report_id")
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;  // 신고 하는 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;    // 신고 당하는 유저

    @Builder
    public Report(Long id, String reason, String content, Feed feed, User fromUser, User toUser) {
        this.id = id;
        this.reason = reason;
        this.content = content;
        this.feed = feed;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

}