package com.project.infootball.repositories

import com.project.infootball.models.Team
import com.project.infootball.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepositoryInterface : JpaRepository<Team, Long> {
    fun findAllByUser(user: User): List<Team>
}