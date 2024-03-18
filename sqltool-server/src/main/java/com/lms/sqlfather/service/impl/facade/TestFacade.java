package com.lms.sqlfather.service.impl.facade;


import com.lms.sqlfather.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class TestFacade {

    public TestFacade(Map<String, UserService> map) {
        System.out.println(map);
    }
}
