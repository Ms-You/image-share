package com.share.image.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final BlockUserService blockUserService;

    // 매 1시간마다 수행
    @Scheduled(cron = "0 0 0/1 * * *")
    public void cancelReportScheduler(){
        blockUserService.liftReportScheduler();
    }

}
