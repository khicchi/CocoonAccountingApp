package com.cocoon.implementation;

import com.cocoon.entity.User;
import com.cocoon.entity.UserLog;
import com.cocoon.enums.ActionType;
import com.cocoon.repository.UserLogRepository;
import com.cocoon.repository.UserRepo;
import com.cocoon.service.UserLogService;
import org.springframework.stereotype.Service;

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

    private ActionType getActionType(String signature) {
        return Stream.of(ActionType.values()).filter(o -> signature.contains(o.getMethodName())).findFirst().orElse(null);
    }
}
