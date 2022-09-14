package com.share.image.feed.domain;

import com.share.image.user.domain.BaseTimeEntity;
import com.share.image.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "subscribe_uk",
                columnNames = {"from_user_id", "to_user_id"}
        )
})
public class Subscribe extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "subscribe_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;  // 구독을 하는 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;    // 구독을 받는 유저

}
