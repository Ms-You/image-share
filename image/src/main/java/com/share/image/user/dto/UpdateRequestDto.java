package com.share.image.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateRequestDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 8, message = "닉네임은 2~8자 사이로 입력해주세요.")
    private String nickName;

    @Size(max = 500, message = "최대 500자까지 작성 가능합니다.")
    private String intro;

}
