package com.share.image.admin.controller;

import com.share.image.admin.domain.Information;
import com.share.image.admin.domain.InformationType;
import com.share.image.admin.dto.InfoDtoValidator;
import com.share.image.admin.dto.InfoRequestDto;
import com.share.image.admin.repository.InformationRepository;
import com.share.image.admin.service.InformationService;
import com.share.image.config.PrincipalDetails;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class InformationController {

    private final UserRepository userRepository;
    private final InformationService informationService;
    private final InformationRepository informationRepository;
    private final InfoDtoValidator infoDtoValidator;


    @ModelAttribute("informationTypes")
    public InformationType[] informationTypes() {
        return InformationType.values();
    }

    // 공지 관리
    @GetMapping("/infos")
    public String infoList(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Information> infos = informationRepository.findAll(pageable);
        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > infos.getTotalPages() ? infos.getTotalPages() : tempEndPage;

        model.addAttribute("infos", infos);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/info/infoList";
    }


    @GetMapping("/new/info")
    public String createInfo(Model model) {
        model.addAttribute("infoRequestDto", new InfoRequestDto());

        return "admin/info/enroll";
    }

    @PostMapping("/new/info")
    public String enrollInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @Valid @ModelAttribute InfoRequestDto infoRequestDto,
                             BindingResult bindingResult, Model model,
                             @RequestParam MultipartFile file) throws UnsupportedEncodingException {

        User writer = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다");
        });

        infoDtoValidator.validate(infoRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("infoRequestDto", infoRequestDto);

            Map<String, String> validatorResult = informationService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }

            return "admin/info/enroll";
        }

        informationService.enrollInfo(writer, infoRequestDto, file);
        model.addAttribute("message", "공지가 등록되었습니다.");

        return "admin/info/enroll";
    }


    // 특정 공지 보기
    @GetMapping("/info/{information_id}")
    public String infoView(@PathVariable(name = "information_id") Long informationId, Model model){

        Information info = informationRepository.findById(informationId).orElseThrow(()->{
            return new IllegalArgumentException("공지를 찾을 수 없습니다.");
        });

        model.addAttribute("info", info);

        return "admin/info/view";
    }

    // 공지 수정페이지 이동
    @GetMapping("/info/update/{information_id}")
    public String updateInfo(@PathVariable(name = "information_id") Long informationId, Model model){

        Information info = informationRepository.findById(informationId).orElseThrow(()->{
            return new IllegalArgumentException("공지를 찾을 수 없습니다.");
        });

        model.addAttribute("info", info);
        model.addAttribute("infoRequestDto", new InfoRequestDto());

        return "admin/info/update";
    }

    // 공지 수정
    @PutMapping("/info/update/{information_id}")
    public String updateInfo(@PathVariable(name = "information_id") Long informationId,
                             @Valid InfoRequestDto infoRequestDto,
                             BindingResult bindingResult, Model model,
                             @RequestParam MultipartFile file) throws UnsupportedEncodingException {

        Information info = informationRepository.findById(informationId).orElseThrow(()->{
            return new IllegalArgumentException("공지를 찾을 수 없습니다.");
        });

        infoDtoValidator.validate(infoRequestDto, bindingResult);

        if (bindingResult.hasErrors()){
            model.addAttribute("infoRequestDto", infoRequestDto);
            model.addAttribute("info", info);

            Map<String, String> validatorResult = informationService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "admin/info/update";
        }
        informationService.updateInfo(info, infoRequestDto, file);

        return "redirect:/admin/infos";
    }


}
