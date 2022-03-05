package com.cocoon.controller;

import com.cocoon.dto.LogSearchDTO;
import com.cocoon.dto.UserDTO;
import com.cocoon.entity.UserLog;
import com.cocoon.enums.ActionType;
import com.cocoon.exception.CocoonException;
import com.cocoon.service.UserLogService;
import com.cocoon.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/logging")
public class LoggingController {
    UserService userService;
    UserLogService userLogService;

    public LoggingController(UserService userService, UserLogService userLogService) {
        this.userService = userService;
        this.userLogService = userLogService;
    }

    @GetMapping("/search")
    public String getLoggingSearchPage(Model model){
        List<UserDTO> users = userService.findAllUsersForLogging();
        model.addAttribute("users", users);
        model.addAttribute("logSearchDTO", new LogSearchDTO());
        model.addAttribute("actionTypes", ActionType.values());
        return "logging/logging-search";
    }

    @PostMapping("/list")
    public String getLoggingSearchResult(@ModelAttribute LogSearchDTO searchDTO, Model model) throws CocoonException {
        List<UserLog> logs = userLogService.findAllLogs(searchDTO);
        model.addAttribute("logs", logs);
        List<UserDTO> users = userService.findAllUsersForLogging();
        model.addAttribute("users", users);
        model.addAttribute("logSearchDTO", new LogSearchDTO());
        model.addAttribute("actionTypes", ActionType.values());
        return "logging/logging-result";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("java8Instant", Instant.now());
    }
}
