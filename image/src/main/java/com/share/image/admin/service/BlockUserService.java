package com.share.image.admin.service;

import com.share.image.config.PrincipalDetails;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockUserService {
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    public void temporaryBlockUser(User findUser){

        if (findUser.getTemporaryLocked().equals("해제하기")){  // 현재 일시 정지 상태
            findUser.updateTemporaryLocked("정지하기"); // 정지 -> 해제
        } else {    // 현재 정지 상태 x
            findUser.updateTemporaryLocked("해제하기");
            findUser.updatePermanentLocked("정지하기");    // 영구 정지였던 경우 -> 일시 정지

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

    public void permanentBlockUser(User findUser){

        if (findUser.getPermanentLocked().equals("해제하기")){   // 현재 영구 정지 상태
            findUser.updatePermanentLocked("정지하기"); // 정지 -> 해제
        } else {
            findUser.updatePermanentLocked("해제하기");
            findUser.updateTemporaryLocked("정지하기");    // 일시 정지였던 경우 -> 영구 정지

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

    // 일시정지 7일 뒤 해제
    public void liftReportScheduler(){
        // 정지 당한 사용자를 대상으로 체크
        List<User> users = userRepository.findAllByTemporaryLocked("해제하기");
        for (User user : users) {
            Duration duration = Duration.between(user.getTemporarySuspendedDate(), LocalDateTime.now());
            if (duration.getSeconds() >= 604800){   // 7일후 정지 해제
                temporaryBlockUser(user);
            }
        }


    }


}
