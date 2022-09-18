package com.share.image.feed.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
public class ReplyRequestDto {

    @NotBlank(message = "댓글내용은 필수 입력 값입니다.")
    @Size(max = 500, message = "최대 500자까지 작성 가능합니다.")
    private String content;

}
