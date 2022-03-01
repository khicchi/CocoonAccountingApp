package com.cocoon.service;

import com.cocoon.entity.UserLog;

public interface UserLogService {
    void save(UserLog userLog);

    void save(String username, String signature);
}
