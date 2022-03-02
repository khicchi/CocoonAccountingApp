package com.cocoon.repository;

import com.cocoon.entity.UserLog;
import com.cocoon.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {

    @Query(value = "SELECT ul FROM  UserLog ul WHERE ul.dateTime BETWEEN ?1 AND ?2 ORDER BY ul.id")
    List<UserLog> findAllByDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT ul FROM  UserLog ul WHERE (ul.dateTime BETWEEN ?1 AND ?2) AND (ul.user.company.id = ?3) ORDER BY ul.id")
    List<UserLog> findCompanyLogsByDates(LocalDateTime startDate, LocalDateTime endDate, Long companyId);

    @Query(value = "SELECT ul FROM  UserLog ul WHERE (ul.dateTime BETWEEN ?1 AND ?2) AND (ul.user.id = ?3) ORDER BY ul.id")
    List<UserLog> findAllByDatesAndUser(LocalDateTime startDate, LocalDateTime endDate, Long userId);

    @Query(value = "SELECT ul FROM  UserLog ul WHERE (ul.dateTime BETWEEN ?1 AND ?2) AND (ul.user.id = ?3) AND (ul.user.company.id = ?4) ORDER BY ul.id")
    List<UserLog> findCompanyLogsByDatesAndUser(LocalDateTime startDate, LocalDateTime endDate, Long userId, Long companyId);

    @Query(value = "SELECT ul FROM  UserLog ul WHERE (ul.dateTime BETWEEN ?1 AND ?2) AND (ul.actionType = ?3) ORDER BY ul.id")
    List<UserLog> findAllByDatesAndActionType(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType);

    @Query(value = "SELECT ul FROM  UserLog ul WHERE (ul.dateTime BETWEEN ?1 AND ?2) AND (ul.actionType = ?3) AND (ul.user.company.id = ?4) ORDER BY ul.id")
    List<UserLog> findCompanyLogsByDatesAndActionType(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType, Long companyId);

    @Query(value = "SELECT ul FROM  UserLog ul WHERE (ul.dateTime BETWEEN ?1 AND ?2) AND (ul.actionType = ?3) AND (ul.user.id = ?4) ORDER BY ul.id")
    List<UserLog> findAllByDatesAndActionTypeAndUser(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType, Long userId);

    @Query(value = "SELECT ul FROM  UserLog ul WHERE (ul.dateTime BETWEEN ?1 AND ?2) AND (ul.actionType = ?3) AND (ul.user.id = ?4) AND (ul.user.company.id = ?5) ORDER BY ul.id")
    List<UserLog> findCompanyLogsByDatesAndActionTypeAndUser(LocalDateTime startDate, LocalDateTime endDate, ActionType actionType, Long userId, Long companyId);
}