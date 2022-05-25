package com.share.image.feed.domain;

import com.share.image.user.domain.User;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class Like {

    @Id @Generated
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Feed feed;
}
