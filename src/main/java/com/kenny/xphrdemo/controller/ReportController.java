package com.kenny.xphrdemo.controller;

import com.kenny.xphrdemo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private LocalDateTime[] validateRange(ModelAndView mav, LocalDateTime from, LocalDateTime to) {
        if (Duration.between(from, to).toDays() > 731) {
            from = to.minusYears(2);
            mav.getModel().put("error", "You can only query for 2 years as max");
        }

        return new LocalDateTime[]{from, to};
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ModelAndView report(Principal principal) {
        ModelAndView mv = new ModelAndView("report");
        mv.getModel().put("user", principal.getName());

        reportService.populateReport(mv, principal.getName());
        return mv;
    }
}
