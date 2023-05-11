package com.project.infootball.repositories

import com.project.infootball.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepositoryInterface : JpaRepository<User, Long> {
    fun findByUsername(username: String) : User?

    fun findByActivationCode(activationCode: String) : User?
}