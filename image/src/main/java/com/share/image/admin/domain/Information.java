package com.share.image.admin.domain;

import com.share.image.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Information {

    @Id @GeneratedValue
    @Column(name = "information_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InformationType informationType;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @Builder
    public Information(String title, String content, InformationType informationType, User writer) {
        this.title = title;
        this.content = content;
        this.informationType = informationType;
        this.writer = writer;
        this.createdDate = LocalDateTime.now();
    }

    public void updateInfo(String title, String content, InformationType informationType) {
        this.title = title;
        this.content = content;
        this.informationType = informationType;
    }

}
