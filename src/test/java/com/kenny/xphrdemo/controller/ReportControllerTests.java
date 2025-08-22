package com.kenny.xphrdemo.controller;

import com.kenny.xphrdemo.dto.ReportRequestDTO;
import com.kenny.xphrdemo.service.ReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import static org.mockito.Mockito.mock;

@SpringBootTest
public class ReportControllerTests {

    @Mock
    private ReportService reportService = mock(ReportService.class);

    @Test
    public void testReport() {
        ReportController reportController = new ReportController(reportService);

        Principal principal = mock(Principal.class);

        ModelAndView modelAndView = reportController.report(principal, new ReportRequestDTO("", ""), 1, 20);
        Assertions.assertNotNull(modelAndView);

        modelAndView = reportController.report(principal, new ReportRequestDTO("2023-01-01T11:00", "2027-01-01T11:00"), 1, 20);
        Assertions.assertNotNull(modelAndView);
        Assertions.assertNotNull(modelAndView.getModel().get("error"));
    }
}
