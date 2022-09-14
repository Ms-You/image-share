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
@Table(name = "views")
public class View extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "view_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public View(Long id, User user, Feed feed) {
        this.id = id;
        this.user = user;
        this.feed = feed;
    }


}
