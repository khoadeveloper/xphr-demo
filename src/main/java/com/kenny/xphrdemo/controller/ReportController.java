package com.kenny.xphrdemo.controller;

import com.kenny.xphrdemo.dto.ReportRequestDTO;
import com.kenny.xphrdemo.service.ReportService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    private LocalDateTime[] validateRange(ModelAndView mav, LocalDateTime from, LocalDateTime to) {
        if (Duration.between(from, to).toDays() > 731) {
            from = to.minusYears(2);
            mav.getModel().put("error", "You can only query for 2 years as max");
        }

        return new LocalDateTime[]{from, to};
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ModelAndView report(Principal principal,
                               @ModelAttribute(name = "request") ReportRequestDTO request,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "50") Integer size) {
        ModelAndView mv = new ModelAndView("report");
        mv.getModel().put("user", principal.getName());

        LocalDateTime fromDate;
        LocalDateTime toDate;

        if (StringUtils.isBlank(request.getFrom())) {
            fromDate = LocalDateTime.now().minusYears(1);
        } else {
            fromDate = LocalDateTime.parse(request.getFrom(), DTF);
        }

        if (StringUtils.isBlank(request.getTo())) {
            toDate = LocalDateTime.now();
        } else {
            toDate = LocalDateTime.parse(request.getTo(), DTF);
        }

        LocalDateTime[] result = this.validateRange(mv, fromDate, toDate);

        mv.getModel().put("request", new ReportRequestDTO(DTF.format(result[0]), DTF.format(result[1])));
        reportService.populateReport(mv, principal.getName(), page, size, fromDate, toDate);
        return mv;
    }
}
