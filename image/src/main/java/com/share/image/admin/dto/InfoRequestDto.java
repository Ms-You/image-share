package com.share.image.admin.dto;

import com.share.image.admin.domain.InformationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
public class InfoRequestDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(min = 1, max = 20, message = "제목은 최대 20자까지 입력해주세요.")
    private String title;

    @Size(max = 500, message = "최대 500자까지 작성 가능합니다.")
    private String content;

    @NotNull(message = "공지 타입을 선택해주세요.")
    private InformationType informationType;

}
