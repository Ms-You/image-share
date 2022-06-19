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
public class ReplyRequestDto {

    private Long id;

    @NotBlank(message = "댓글내용은 필수 입력 값입니다.")
    @Size(max = 500, message = "최대 500자까지 작성 가능합니다.")
    private String content;

    @Builder
    public ReplyRequestDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

}
