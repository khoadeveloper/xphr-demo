package com.kenny.xphrdemo.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest
public class HomeControllerTests {

    @Autowired
    private HomeController homeController;

    @Test
    public void testIndex() {
        ModelAndView mav = homeController.index();
        Assertions.assertNotNull(mav);
        Assertions.assertEquals("index", mav.getViewName());
    }
}
