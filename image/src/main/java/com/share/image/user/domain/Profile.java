package com.share.image.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Profile {

    @Id @GeneratedValue
    @Column(name = "profile_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String profileImageUrl;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String intro;

    public void uploadUser(User user){
        this.user = user;
    }

    public void updateProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfile(String nickName, String intro){
        this.user.modifyNickName(nickName);
        this.intro = intro;
    }

}
