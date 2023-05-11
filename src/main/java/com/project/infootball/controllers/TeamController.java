package com.project.infootball.controllers;

import com.project.infootball.data_classes.TeamData;
import com.project.infootball.models.Team;
import com.project.infootball.services.TeamService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping("/follow")
    public void addTeamToFollowed(@RequestBody TeamData teamData, HttpServletRequest request) {
        teamService.addFavoriteTeam(teamData, request);
    }

    @DeleteMapping("/delete/{teamId}")
    public void deleteTeamFromFollowed(@PathVariable String teamId) {
        teamService.deleteFavoriteTeam(teamId);
    }

    @GetMapping("/list")
    @NotNull
    public List<Team> getFollowedTeams(HttpServletRequest request) {
        return teamService.getFavoriteTeams(request);
    }
}