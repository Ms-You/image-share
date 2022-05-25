package com.share.image.feed.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Tag {

    @Id @Generated
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany
    private List<Feed> feeds;

}
