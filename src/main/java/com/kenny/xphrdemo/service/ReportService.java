package com.kenny.xphrdemo.service;

import com.kenny.xphrdemo.repository.TimeRecordRepository;
import com.kenny.xphrdemo.repository.projections.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Service
public class ReportService {

    private static final int MAX_PAGE_SIZE = 200;
    private final UserDetailsService userDetailsService;
    private final TimeRecordRepository timeRecordRepository;

    @Autowired
    public ReportService(UserDetailsService userDetailsService, TimeRecordRepository timeRecordRepository) {
        this.userDetailsService = userDetailsService;
        this.timeRecordRepository = timeRecordRepository;
    }

    public void populateReport(ModelAndView mav, String user, int page, int size,
                               LocalDateTime fromDate, LocalDateTime toDate) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        PageRequest pageRequest = PageRequest.of(page - 1, Math.min(size, MAX_PAGE_SIZE));
        mav.getModel().put("page", page);

        Page<Report> pageData;
        if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            pageData = timeRecordRepository.getReports(fromDate, toDate, null, pageRequest);
        } else {
            pageData = timeRecordRepository.getReports(fromDate, toDate, user, pageRequest);
        }
        mav.getModel().put("report", pageData.getContent());

        mav.getModel().put("maxPage", pageData.getTotalPages());
    }
}
