package com.share.image.admin.repository;

import com.share.image.admin.domain.Information;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationRepository extends JpaRepository<Information, Long> {
}
