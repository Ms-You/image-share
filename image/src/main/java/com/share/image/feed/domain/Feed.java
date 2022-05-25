package com.share.image.feed.domain;

import com.share.image.user.domain.User;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Feed {

    @Id @Generated
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String uploadImage;

    @ManyToOne
    private User writer;

    @ManyToOne
    private Tag tag;

    @OneToMany
    private List<Like> likes;

    @OneToMany
    private List<Reply> replies;

    @CreatedDate
    private LocalDateTime createdDate;


}
