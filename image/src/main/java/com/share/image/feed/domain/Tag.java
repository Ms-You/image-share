package com.share.image.feed.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Tag {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String tagImageUrl;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();

    @Builder
    public Tag(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public void updateTagName(String name){
        this.name = name;
    }

    public void updateTagImageUrl(String tagImageUrl){
        this.tagImageUrl = tagImageUrl;
    }


}
