package com.share.image.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Profile {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = true)
    private String intro;



}
