package com.share.image.admin.controller;

import com.share.image.admin.dto.TagDtoUpdateValidator;
import com.share.image.admin.dto.TagDtoValidator;
import com.share.image.admin.dto.TagRequestDto;
import com.share.image.admin.service.AdminTagService;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
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
    private final TagDtoUpdateValidator tagDtoUpdateValidator;
    private final AdminTagService adminTagService;
    private final TagRepository tagRepository;
    private final FeedRepository feedRepository;

    // 태그 모음 페이지로 이동
    @GetMapping("/tags")
    public String tags(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<Tag> tags = tagRepository.findAll(pageable);
        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > tags.getTotalPages() ? tags.getTotalPages() : tempEndPage;

        model.addAttribute("tags", tags);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/admin/tag/tags";
    }

    // 태그 생성 페이지로 이동
    @GetMapping("/new/tag")
    public String createTag(){
        return "/admin/tag/new_tag";
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

            return "admin/tag/new_tag";
        }

        adminTagService.createTag(tagRequestDto, file);
        return "redirect:/admin/tags";
    }


    // 태그 수정페이지 이동
    @GetMapping("/tag/update/{tag_id}")
    public String updateTag(@PathVariable(name = "tag_id") Long tagId, Model model){

        Tag tag = tagRepository.findById(tagId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 태그입니다.");
        });
        model.addAttribute("tag", tag);

        return "admin/tag/update";
    }

    // 태그 수정
    @PutMapping("/tag/update/{tag_id}")
    public String updateTag(@PathVariable(name = "tag_id") Long tagId,
                            @Valid TagRequestDto tagRequestDto,
                            Errors errors,
                            Model model,
                            @RequestParam MultipartFile file) throws UnsupportedEncodingException {

        Tag tag = tagRepository.findById(tagId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 태그입니다.");
        });

        // 파일 유효성 검사를 위해 파일명을 dto 에 넣어줌
        tagRequestDto.insertImage(file.getOriginalFilename());


        // tagRequestDto 와 tag 의 name 이 같을 경우 파일 존재 여부만 체크
        if (!tag.getName().equals(tagRequestDto.getName())){
            tagDtoValidator.validate(tagRequestDto, errors);
        } else {
            tagDtoUpdateValidator.validate(tagRequestDto, errors);
        }


        // 유효성 검사
        if (errors.hasErrors()){
            model.addAttribute("tagRequestDto", tagRequestDto);
            model.addAttribute("tag", tag);

            Map<String, String> validatorResult = adminTagService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "admin/tag/update";
        }
        adminTagService.updateTag(tag, tagRequestDto, file);

        return "redirect:/admin/tags";
    }


    // 태그 삭제
    @GetMapping("/delete/{tag_id}")
    public String deleteTag(@PathVariable("tag_id") Long tagId) {
        tagRepository.deleteById(tagId);

        return "redirect:/admin/tags";
    }


    // 태그 목록 보기 (이후 태그 목록 확인 가능)
    @GetMapping("/tag/list")
    public String tagList(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<Tag> tags = tagRepository.findAll(pageable);
        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > tags.getTotalPages() ? tags.getTotalPages() : tempEndPage;

        model.addAttribute("tags", tags);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/tag/tagList";
    }


    // 피드 모음 페이지로 이동
    @GetMapping("/tag")
    public String tags(@RequestParam(name = "tag_id") Long tagId, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Tag tag = tagRepository.findById(tagId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 태그입니다.");
        });

        Page<Feed> feeds = feedRepository.findByTag(tag, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > feeds.getTotalPages() ? feeds.getTotalPages() : tempEndPage;

        model.addAttribute("tag", tag);
        model.addAttribute("feeds", feeds);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "admin/feed/feeds";
    }


}
