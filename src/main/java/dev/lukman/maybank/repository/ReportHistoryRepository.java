package dev.lukman.maybank.repository;

import dev.lukman.maybank.model.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportHistoryRepository extends JpaRepository<ReportHistory, Long> {
    Optional<ReportHistory> findByFileId(String id);
}
