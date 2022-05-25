package com.share.image.feed.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Tag {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany
    private List<Feed> feeds;

}
