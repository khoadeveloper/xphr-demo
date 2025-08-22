package com.kenny.xphrdemo.service;

import com.kenny.xphrdemo.repository.TimeRecordRepository;
import com.kenny.xphrdemo.repository.projections.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TimeRecordRepository timeRecordRepository;

    public void populateReport(ModelAndView mav, String user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        List<Report> pageData;
        if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            pageData = timeRecordRepository.getReports( null);
        } else {
            pageData = timeRecordRepository.getReports(user);
        }
        mav.getModel().put("report", pageData);

        mav.getModel().put("maxPage", pageData);
    }
}
