package com.share.image.user.domain;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Like;
import com.share.image.feed.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private int age;

    @OneToOne
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany
    private List<Feed> feeds;

    @OneToMany
    private List<Like> likes;

    @OneToMany
    private List<Reply> replies;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Subscribe subscribe;

    @CreatedDate
    private LocalDateTime createdDate;


}
