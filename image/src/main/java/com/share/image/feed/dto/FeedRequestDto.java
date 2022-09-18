package com.share.image.feed.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
public class FeedRequestDto {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(min = 1, max = 20, message = "제목은 최대 20자까지 입력해주세요.")
    private String title;

    @NotBlank(message = "설명은 필수 입력 값입니다.")
    @Size(max = 500, message = "최대 500자까지 작성 가능합니다.")
    private String description;

    private String feedImageUrl;

    // 파일 유효성 검사를 위해 파일명을 넣어줌
    public void insertImage(String fileName){
        this.feedImageUrl = fileName;
    }
}
