package com.share.image.user.domain;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Like;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.domain.Subscribe;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToMany
    private List<Feed> feeds;

    @OneToMany
    private List<Like> likes;

    @OneToMany
    private List<Reply> replies;

    @OneToMany
    private List<Subscribe> subscribes;



}
