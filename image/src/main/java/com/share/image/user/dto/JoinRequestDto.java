package com.share.image.user.dto;

import com.share.image.user.domain.RoleType;
import com.share.image.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JoinRequestDto {
    private Long id;
    private String email;
    private String password;
    private String nickName;

    public User toEntity(){
        return User.builder()
                .id(id)
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .nickName(nickName)
                .role(RoleType.USER)
                .build();
    }

}
