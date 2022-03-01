package com.cocoon.repository;

import com.cocoon.entity.UserLog;
import com.cocoon.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    List<UserLog> findAllByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<UserLog> findAllByDateTimeBetweenAndUserCompanyId(LocalDateTime startDate, LocalDateTime endDate, Long companyId);

    List<UserLog> findAllByDateTimeBetweenAndUser_Id(LocalDateTime startDate, LocalDateTime endDate, Long user_id);

    List<UserLog> findAllByDateTimeBetweenAndUserIdAndUserCompanyId(LocalDateTime startDate, LocalDateTime endDate, Long user_id, Long companyId);

    List<UserLog> findAllByDateTimeBetweenAndActionType(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType);

    List<UserLog> findAllByDateTimeBetweenAndActionTypeAndUserCompanyId(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType, Long companyId);

    List<UserLog> findAllByDateTimeBetweenAndActionTypeAndUser_Id(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType, Long user_id);

    List<UserLog> findAllByDateTimeBetweenAndActionTypeAndUserIdAndUserCompanyId(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType, Long user_id, Long companyId);

}