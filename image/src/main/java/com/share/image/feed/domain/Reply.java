package com.share.image.feed.domain;

import com.share.image.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Reply {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private User writer;

    @ManyToOne
    private Feed feed;

    @CreatedDate
    private LocalDateTime createdDate;

}
