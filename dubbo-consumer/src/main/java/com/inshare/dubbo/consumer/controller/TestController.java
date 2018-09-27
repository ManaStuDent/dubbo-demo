package com.inshare.dubbo.consumer.controller;

import com.inshare.dubbo.consumer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dubbo")
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/show")
    public String show(@RequestParam("id") long id) {
        return  testService.show(id);
    }
}
