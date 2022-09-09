package com.share.image.admin.controller;

import com.share.image.admin.service.BlockUserService;
import com.share.image.feed.domain.Report;
import com.share.image.feed.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/report")
public class AdminReportController {
    private final BlockUserService blockUserService;
    private final ReportRepository reportRepository;

    // 신고 목록 페이지 이동
    @GetMapping("")
    public String reportList(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){

        Page<Report> reports = reportRepository.findAll(pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > reports.getTotalPages() ? reports.getTotalPages() : tempEndPage;

        model.addAttribute("reports", reports);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/report/reportList";
    }


    // 특정 신고 보기
    @GetMapping("/{reportId}")
    public String reportView(@PathVariable(name = "reportId") Long reportId, Model model){

        Report report = reportRepository.findById(reportId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 신고입니다.");
        });

        model.addAttribute("report", report);

        return "admin/report/reportView";
    }

    // 계정 일시 정지하기
    @GetMapping("/temporary/user/{userId}")
    public String temporaryStoppedUser(@PathVariable(name = "userId") Long userId){
        blockUserService.temporaryBlockUser(userId);

        return "redirect:/admin/report";
    }

    // 계정 영구 정지하기
    @GetMapping("/permanent/user/{userId}")
    public String permanentStoppedUser(@PathVariable(name = "userId") Long userId){
        blockUserService.permanentBlockUser(userId);

        return "redirect:/admin/report";
    }


}
