package com.cocoon.service;

import com.cocoon.dto.LogSearchDTO;
import com.cocoon.entity.UserLog;
import com.cocoon.exception.CocoonException;

import java.util.List;

public interface UserLogService {
    void save(UserLog userLog);

    void save(String username, String signature);

    List<UserLog> findAllLogs(LogSearchDTO searchDTO) throws CocoonException;
}
