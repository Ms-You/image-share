package com.share.image.admin.controller;

import com.share.image.admin.dto.TagDtoValidator;
import com.share.image.admin.dto.TagRequestDto;
import com.share.image.admin.service.AdminTagService;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminTagController {

    private final TagDtoValidator tagDtoValidator;
    private final AdminTagService adminTagService;
    private final TagRepository tagRepository;

    // 태그 모음 페이지로 이동
    @GetMapping("/tags")
    public String tags(Model model){
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);

        return "/admin/tags";
    }

    // 태그 생성 페이지로 이동
    @GetMapping("/new/tag")
    public String createTag(){
        return "/admin/new_tag";
    }

    // 태그 생성
    @PostMapping("/new/tag")
    public String newTag(@Valid TagRequestDto tagRequestDto, Errors errors, Model model, @RequestParam MultipartFile file) throws UnsupportedEncodingException {
        // 파일 유효성 검사를 위해 파일명을 dto 에 넣어줌
        tagRequestDto.insertImage(file.getOriginalFilename());
        tagDtoValidator.validate(tagRequestDto, errors);

        if (errors.hasErrors()){
            model.addAttribute("tagRequestDto", tagRequestDto);

            Map<String, String> validatorResult = adminTagService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "admin/new_tag";
        }

        adminTagService.createTag(tagRequestDto, file);
        return "redirect:/admin/tags";
    }


}
