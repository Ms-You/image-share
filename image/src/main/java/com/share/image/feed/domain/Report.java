package com.share.image.feed.domain;

import com.share.image.user.domain.BaseTimeEntity;
import com.share.image.user.domain.User;
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


    //== 생성 메서드 ==//
    public static Report createReport(User fromUser, User toUser, Feed feed, String reason, String content){
        Report report = new Report();
        report.reason = reason;
        report.content = content;
        report.feed = feed;
        report.fromUser = fromUser;
        report.toUser = toUser;

        feed.getReports().add(report);

        return report;
    }

}