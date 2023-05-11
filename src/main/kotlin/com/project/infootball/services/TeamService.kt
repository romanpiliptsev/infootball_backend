package com.project.infootball.services

import com.project.infootball.data_classes.TeamData
import com.project.infootball.models.Team
import com.project.infootball.repositories.TeamRepositoryInterface
import com.project.infootball.repositories.UserRepositoryInterface
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class TeamService (
    private val teamRepositoryInterface: TeamRepositoryInterface,
    private val userRepositoryInterface: UserRepositoryInterface,
    private val authService: AuthService
) {
    fun addFavoriteTeam(teamData: TeamData, request: HttpServletRequest) {
        val token: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        val username = authService.verifyTokenAndGetJws(token).body.get("username", String::class.java)
        val userFromDb = userRepositoryInterface.findByUsername(username)

        Team().apply {
            teamCode = teamData.teamCode
            teamEmblemUrl = teamData.teamEmblemLink
            teamName = teamData.teamName
            user = userFromDb!!

            teamRepositoryInterface.save(this)
        }
    }

    fun deleteFavoriteTeam(teamId: String) {
        teamRepositoryInterface.deleteById(teamId.toLong())
    }

    fun getFavoriteTeams(request: HttpServletRequest): List<Team> {
        val token: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        val username = authService.verifyTokenAndGetJws(token).body.get("username", String::class.java)
        val userFromDb = userRepositoryInterface.findByUsername(username)

        return teamRepositoryInterface.findAllByUser(userFromDb!!)
    }
}