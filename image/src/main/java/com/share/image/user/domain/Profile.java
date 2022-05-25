package com.share.image.user.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@NoArgsConstructor
@Entity
public class Profile {

    @Id @Generated
    private Long id;

    @OneToOne
    private User user;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = true)
    private String intro;



}
