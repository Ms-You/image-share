package com.share.image.user.domain;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Like;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.domain.Subscribe;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String profileImageUrl;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String intro;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany
    private List<Subscribe> subscribes = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public User(Long id, String email, String nickName, String password, RoleType role, String intro, String profileImageUrl){
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.role = role;
        this.intro = intro;
        this.profileImageUrl = profileImageUrl;
        this.createdDate = LocalDateTime.now();
    }

    public void updateProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfile(String nickName, String intro){
        this.nickName = nickName;
        this.intro = intro;
    }

}
