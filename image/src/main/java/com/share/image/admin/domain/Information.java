package com.share.image.admin.domain;

import com.share.image.user.domain.BaseTimeEntity;
import com.share.image.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Information extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "information_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String infoImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InformationType informationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;


    //== 생성 메서드 ==//
    public static Information createInformation(String title, String content, InformationType informationType, User writer){
        Information information = new Information();
        information.writer = writer;
        information.updateInfo(title, content, informationType);
        writer.getInformations().add(information);

        return information;
    }

    public void updateInfo(String title, String content, InformationType informationType) {
        this.title = title;
        this.content = content;
        this.informationType = informationType;
    }

    public void updateInfoImageUrl(String infoImageUrl){
        this.infoImageUrl = infoImageUrl;
    }

}
