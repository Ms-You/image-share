package com.share.image.admin.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
public class TagRequestDto {

    @NotBlank(message = "태그명은 필수 입력 값입니다.")
    @Size(min = 1, max = 8, message = "태그명은 최대 8자까지 입력해주세요.")
    private String name;

    private String tagImageUrl;

    // 파일 유효성 검사를 위해 파일명을 넣어줌
    public void insertImage(String fileName){
        this.tagImageUrl = fileName;
    }

}
