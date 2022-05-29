package com.share.image.user.dto;

import com.share.image.user.domain.Gender;
import com.share.image.user.domain.User;
import lombok.Getter;

@Getter
public class JoinRequestDto {

    private String email;
    private String password;
    private String nickName;
    private Gender gender;


}
