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

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String intro;



}
