package com.share.image.feed.domain;

import com.share.image.user.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Tag extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String tagImageUrl;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();


    //== 생성 메서드 ==//
    public static Tag createTag(String name){
        Tag tag = new Tag();
        tag.updateTag(name);

        return tag;
    }

    public void updateTag(String name){
        this.name = name;
    }

    public void updateTagImageUrl(String tagImageUrl){
        this.tagImageUrl = tagImageUrl;
    }


}
