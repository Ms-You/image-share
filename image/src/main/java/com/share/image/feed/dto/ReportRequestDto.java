package com.share.image.feed.dto;

import com.share.image.feed.domain.ReportType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
public class ReportRequestDto {
    private Long id;
    @Size(max = 500, message = "최대 500자까지 작성 가능합니다.")
    private String content;

    @NotNull(message = "신고 사유를 입력해주세요")
    private ReportType reportType;

    @Builder
    public ReportRequestDto(Long id, String content){
        this.id = id;
        this.content = content;
    }
}
