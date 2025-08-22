package com.kenny.xphrdemo.service;

import com.kenny.xphrdemo.repository.TimeRecordRepository;
import com.kenny.xphrdemo.repository.projections.Report;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReportServiceTests {

    @Mock
    private TimeRecordRepository timeRecordRepository = mock(TimeRecordRepository.class);

    @Mock
    private UserDetailsService userDetailsService = mock(UserDetailsService.class);

    private Report createReport() {
        return new Report() {
            @Override
            public String getUser() {
                return "user";
            }

            @Override
            public String getProject() {
                return "project";
            }

            @Override
            public Long getHours() {
                return 100L;
            }
        };
    }

    @Test
    public void testPopulateReport() {
        ReportService reportService = new ReportService(userDetailsService, timeRecordRepository);
        User admin = new User("testadmin", "123456", Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        User employee = new  User("testemployee", "123456", Collections.singleton(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));

        when(userDetailsService.loadUserByUsername("testadmin")).thenReturn(admin);
        when(userDetailsService.loadUserByUsername("testemployee")).thenReturn(employee);

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMonths(3);

        PageRequest pageRequest = PageRequest.of(0, 10);
        when(timeRecordRepository.getReports(from, to, null, pageRequest))
                .thenReturn(new PageImpl<>(Arrays.asList(createReport(), createReport(), createReport())));

        when(timeRecordRepository.getReports(from, to, "testemployee", pageRequest))
                .thenReturn(new PageImpl<>(Collections.singletonList(createReport())));

        ModelAndView modelAndView = new ModelAndView();
        reportService.populateReport(modelAndView, "testadmin", 1, 10, from, to);
        Assertions.assertNotNull(modelAndView.getModel().get("report"));
        List<Report> reports = (List<Report>) modelAndView.getModel().get("report");
        Assertions.assertEquals(3, reports.size());

        reportService.populateReport(modelAndView, "testemployee", 1, 10, from, to);
        Assertions.assertNotNull(modelAndView.getModel().get("report"));
        reports = (List<Report>) modelAndView.getModel().get("report");
        Assertions.assertEquals(1, reports.size());
    }
}
