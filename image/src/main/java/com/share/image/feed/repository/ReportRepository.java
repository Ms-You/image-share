package com.share.image.feed.repository;

import com.share.image.feed.domain.Report;
import com.share.image.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "select count(*) from report where to_user_id = :toUserId", nativeQuery = true)
    Integer findReportCountByUserId(@Param(value = "toUserId") Long toUserId);

    Optional<Report> findByFromUserAndToUser(User fromUser, User toUser);
}
