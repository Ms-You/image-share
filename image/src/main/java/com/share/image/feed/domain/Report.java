package com.share.image.feed.domain;

import com.share.image.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Report {

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

    @CreatedDate
    private LocalDateTime reportDate;

    @Builder
    public Report(Long id, String reason, String content, Feed feed, User fromUser, User toUser) {
        this.id = id;
        this.reason = reason;
        this.content = content;
        this.feed = feed;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.reportDate = LocalDateTime.now();
    }

}