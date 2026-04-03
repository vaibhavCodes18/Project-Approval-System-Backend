package com.bit.ProjectApprovalSystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    String sayHello(){
        return "Welcome to Project Approval System Backend!";
    }
}
