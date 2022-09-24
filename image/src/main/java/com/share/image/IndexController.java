package com.share.image;

import com.share.image.admin.domain.Information;
import com.share.image.admin.repository.InformationRepository;
import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.user.dto.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final InformationRepository informationRepository;

    @GetMapping({"", "/"})
    public String index(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Information> infos = informationRepository.findAll(pageable);
        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > infos.getTotalPages() ? infos.getTotalPages() : tempEndPage;

        model.addAttribute("infos", infos);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "index";
    }


    // 특정 공지 보기
    @GetMapping("/info/{information_id}")
    public String viewInfo(@PathVariable(name = "information_id") Long informationId, Model model){

        Information info = informationRepository.findById(informationId).orElseThrow(
                ()-> new GlobalException(ErrorCode.RESOURCE_ERROR)
        );

        model.addAttribute("info", info);

        return "info/view";
    }


    // 회원가입 페이지 이동
    @GetMapping("/auth/join")
    public String join(JoinRequestDto joinRequestDto){
        return "/user/join";
    }


    // 로그인 페이지 이동
    @GetMapping("/auth/login")
    public String login(
            @RequestParam(value = "error", required = false)String error,
            @RequestParam(value = "exception", required = false)String exception,
            Model model, HttpServletRequest httpServletRequest){

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        Cookie[] myCookies = httpServletRequest.getCookies();
        if (myCookies == null || myCookies.length <= 1) {   // 쿠키에 userEmail, userPassword 가 없을 경우 체크박스 해제
            model.addAttribute("flag", false);
        }
        else if (myCookies != null){
            for(int i = 0; i < myCookies.length; i++) {
                model.addAttribute(myCookies[i].getName(), myCookies[i].getValue());
            }
            model.addAttribute("flag", true);
        }

        return "/user/login";
    }


    // 로그인 실패 시 이동
    @PostMapping("/auth/login")
    public String fail(){
        return "user/login";
    }

}
