package com.cocoon.implementation;

import com.cocoon.dto.LogSearchDTO;
import com.cocoon.entity.User;
import com.cocoon.entity.UserLog;
import com.cocoon.enums.ActionType;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.UserLogRepository;
import com.cocoon.repository.UserRepo;
import com.cocoon.service.UserLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class UserLogServiceImpl implements UserLogService {

    UserLogRepository userLogRepository;
    UserRepo userRepo;

    public UserLogServiceImpl(UserLogRepository userLogRepository, UserRepo userRepo) {
        this.userLogRepository = userLogRepository;
        this.userRepo = userRepo;
    }

    @Override
    public void save(UserLog userLog) {
        userLogRepository.save(userLog);
    }

    @Override
    public void save(String username, String signature) {
        User user = userRepo.findByEmail(username);
        UserLog userLog = new UserLog();
        userLog.setUser(user);
        ActionType actionType = getActionType(signature);
        if (actionType != null) {
            userLog.setActionType(actionType);
            userLogRepository.save(userLog);
        }
    }

    @Override
    public List<UserLog> findAllLogs(LogSearchDTO searchDTO) throws CocoonException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROOT")) {
            return retrieveAllLogs(searchDTO);
        } else {
            Long companyId = userRepo.findByEmail(authentication.getName()).getCompany().getId();
            return retrieveCompanyLogs(searchDTO, companyId);
        }
    }

    private ActionType getActionType(String signature) {
        return Stream.of(ActionType.values()).filter(o -> signature.contains(o.getMethodName())).findFirst().orElse(null);
    }

    private List<UserLog> retrieveAllLogs(LogSearchDTO parameters) throws CocoonException {
        if (parameters.getUser() != null && parameters.getActionType() != null) {
            return userLogRepository.findAllByDateTimeBetweenAndActionTypeAndUser_Id(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59), parameters.getActionType(), parameters.getUser().getId());
        } else if (parameters.getUser() == null && parameters.getActionType() == null) {
            return userLogRepository.findAllByDateTimeBetween(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59));
        } else if (parameters.getUser() != null) {
            return userLogRepository.findAllByDateTimeBetweenAndUser_Id(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59), parameters.getUser().getId());
        } else if (parameters.getActionType() != null) {
            return userLogRepository.findAllByDateTimeBetweenAndActionType(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59), parameters.getActionType());
        }
        throw new CocoonException("Insufficient parameter for log search");
    }

    private List<UserLog> retrieveCompanyLogs(LogSearchDTO parameters, Long companyId) throws CocoonException {
        if (parameters.getUser() != null && parameters.getActionType() != null) {
            return userLogRepository.findAllByDateTimeBetweenAndActionTypeAndUserIdAndUserCompanyId(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59), parameters.getActionType(), parameters.getUser().getId(), companyId);
        } else if (parameters.getUser() == null && parameters.getActionType() == null) {
            return userLogRepository.findAllByDateTimeBetweenAndUserCompanyId(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59), companyId);
        } else if (parameters.getUser() != null) {
            return userLogRepository.findAllByDateTimeBetweenAndUserIdAndUserCompanyId(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59), parameters.getUser().getId(), companyId);
        } else if (parameters.getActionType() != null) {
            return userLogRepository.findAllByDateTimeBetweenAndActionTypeAndUserCompanyId(parameters.getStartDate().atStartOfDay(), parameters.getEndDate().atTime(23, 59), parameters.getActionType(), companyId);
        }
        throw new CocoonException("Insufficient parameter for log search");
    }
}
