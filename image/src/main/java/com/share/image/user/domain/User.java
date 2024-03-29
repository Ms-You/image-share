package com.share.image.user.domain;

import com.share.image.admin.domain.Information;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTimeEntity{

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

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Information> informations = new ArrayList<>();

    private String provider;

    private String providerId;


    @Builder
    public User(String email, String nickName, String password, String provider, String providerId){
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.role = RoleType.ROLE_USER;
        this.provider = provider;
        this.providerId = providerId;
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

    public void updateTemporaryLocked(String locked){
        this.temporaryLocked = locked;
        this.temporarySuspendedDate = LocalDateTime.now();
    }

    public void updatePermanentLocked(String locked){
        this.permanentLocked = locked;
        this.permanentSuspendedDate = LocalDateTime.now();
    }

}
