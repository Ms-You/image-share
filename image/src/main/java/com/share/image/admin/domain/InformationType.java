package com.share.image.admin.domain;

import lombok.Getter;

@Getter
public enum InformationType {
    NOTICE("공지사항"), EVENT("이벤트"), UPDATE("업데이트");

    private final String value;

    InformationType(String value) {
        this.value = value;
    }

}
