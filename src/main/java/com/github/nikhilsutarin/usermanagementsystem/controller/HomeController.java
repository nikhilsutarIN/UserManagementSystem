package com.github.nikhilsutarin.usermanagementsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> homeController(HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.OK.value());
        body.put("error", "None");
        body.put("message", "You are on home endpoint, see documentation for more information - © Nikhil Sutar 2026");
        body.put("path", request.getServletPath());
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
