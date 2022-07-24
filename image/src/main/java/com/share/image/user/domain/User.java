package com.share.image.user.domain;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.FeedLike;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.domain.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(nullable = false)
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

    private String temporaryLocked;

    private String permanentLocked;

    @CreatedDate
    private LocalDateTime temporarySuspendedDate;

    @CreatedDate
    private LocalDateTime permanentSuspendedDate;

    @OneToMany
    private List<Subscribe> subscribes = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FeedLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    private String provider;

    private String providerId;


    @Builder
    public User(String email, String nickName, String password, RoleType role, String intro, String profileImageUrl){
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.role = role;
        this.intro = intro;
        this.profileImageUrl = profileImageUrl;
        this.createdDate = LocalDateTime.now();
        this.permanentLocked = "정지하기";
        this.temporaryLocked = "정지하기";
    }

    public User(String email, String nickName, String password, RoleType role, String profileImageUrl, String provider, String providerId){
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.role = role;
        this.intro = null;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.createdDate = LocalDateTime.now();
        this.permanentLocked = "정지하기";
        this.temporaryLocked = "정지하기";
    }

    public void updateProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfile(String nickName, String intro){
        this.nickName = nickName;
        this.intro = intro;
    }

    public void setTemporaryLocked(String locked){
        this.temporaryLocked = locked;
    }

    public void setPermanentLocked(String locked){
        this.permanentLocked = locked;
    }

    public void recordTemporarySuspendTime(){
        this.temporarySuspendedDate = LocalDateTime.now();
    }

    public void recordPermanentSuspendTime(){
        this.permanentSuspendedDate = LocalDateTime.now();
    }

}
