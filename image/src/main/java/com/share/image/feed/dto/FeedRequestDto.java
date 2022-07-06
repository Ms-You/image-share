package com.share.image.feed.dto;

import com.share.image.feed.domain.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
public class FeedRequestDto {
    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(min = 1, max = 20, message = "제목은 최대 20자까지 입력해주세요.")
    private String title;

    @NotBlank(message = "설명은 필수 입력 값입니다.")
    @Size(max = 500, message = "최대 500자까지 작성 가능합니다.")
    private String description;

    private String feedImageUrl;

    private Tag tag;

    @Builder
    public FeedRequestDto(Long id, String title, String description, String feedImageUrl, Tag tag){
        this.id = id;
        this.title = title;
        this.description = description;
        this.feedImageUrl = feedImageUrl;
        this.tag = tag;
    }

    // 파일 유효성 검사를 위해 파일명을 넣어줌
    public void insertImage(String fileName){
        this.feedImageUrl = fileName;
    }

    public void insertTag(Tag tag){
        this.tag = tag;
    }
}
