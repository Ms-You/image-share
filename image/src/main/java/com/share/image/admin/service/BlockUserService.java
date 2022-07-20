package com.share.image.admin.service;

import com.share.image.config.PrincipalDetails;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockUserService {
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    public void blockUser(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        if (findUser.getLocked().equals("해제하기")){
            findUser.setLocked("정지하기"); // 정지 -> 해제
        } else {
            findUser.setLocked("해제하기"); // 해제 -> 정지
            List<PrincipalDetails> principals = sessionRegistry.getAllPrincipals()
                    .stream().map(o -> (PrincipalDetails) o).collect(Collectors.toList());

            for (PrincipalDetails principal : principals) {
                User user = principal.getUser();
                if (user.getId() == findUser.getId()){
                    List<SessionInformation> sessionList = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessionList){
                        session.expireNow();
                    }
                }

            }
        }


    }
}
