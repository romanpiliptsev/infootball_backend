package com.project.infootball.controllers;

import com.project.infootball.services.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @Autowired
    private ApiService apiService;

    @GetMapping("/matches")
    public String getTodayMatches() {
        return apiService.getTodayMatches();
    }

    @GetMapping("/scorers/{code}")
    public String getScorersByCode(@PathVariable("code") String code) {
        return apiService.getScorersByCode(code);
    }

    @GetMapping("/standings/{code}")
    public String getStandingsByCode(@PathVariable("code") String code) {
        return apiService.getStandingsByCode(code);
    }

    @GetMapping("/teams/{code}")
    public String getTeamMatches(@PathVariable("code") String code) {
        return apiService.getTeamMatches(code);
    }
}