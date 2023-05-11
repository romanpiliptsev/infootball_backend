package com.project.infootball.services

import com.project.infootball.data_classes.RegistrationData
import com.project.infootball.models.Role
import com.project.infootball.models.User
import com.project.infootball.repositories.UserRepositoryInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    @Autowired
    private val userRepositoryInterface: UserRepositoryInterface,
    @Autowired
    private val emailService: EmailService
) : UserDetailsService {

    fun addUser(registrationData: RegistrationData): Boolean {
        User().apply {
            username = registrationData.username
            email = registrationData.email
            isActive = false
            activationCode = UUID.randomUUID().toString()
            password = BCryptPasswordEncoder(8).encode(registrationData.password)
            role = Role.ROLE_USER

            userRepositoryInterface.save(this)

            // fix the link
            emailService.sendEmail(
                email,
                "Активация аккаунта",
                "Привет, ${username}!\nПерейди по ссылке, чтобы активировать аккаунт INFootball: http://localhost:3000/user/${activationCode}"
            )
        }
        return true
    }

    fun checkUsername(username: String): User? {
        return userRepositoryInterface.findByUsername(username)
    }

    fun getUserInfo(username: String): User? {
        return userRepositoryInterface.findByUsername(username)
    }

    fun removeUser(username: String): Boolean {
        (userRepositoryInterface.findByUsername(username) ?: return false).apply {
            userRepositoryInterface.deleteById(this.userId)
        }
        return true
    }

    fun updateUsername(username: String, newUsername: String): Boolean {
        (userRepositoryInterface.findByUsername(username) ?: return false).apply {
            this.username = newUsername
            userRepositoryInterface.save(this)
        }
        return true
    }

    fun updateEmail(username: String, email: String): Boolean {
        (userRepositoryInterface.findByUsername(username) ?: return false).apply {
            this.email = email
            userRepositoryInterface.save(this)
        }
        return true
    }

    fun updatePassword(username: String, password: String): Boolean {
        (userRepositoryInterface.findByUsername(username) ?: return false).apply {
            this.password = BCryptPasswordEncoder(8).encode(password)
            userRepositoryInterface.save(this)
        }
        return true
    }

    fun activateUser(code: String): Boolean {
        (userRepositoryInterface.findByActivationCode(code) ?: return false).apply {
            this.isActive = true
            this.activationCode = null

            userRepositoryInterface.save(this)
        }
        return true
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        return userRepositoryInterface.findByUsername(username)
    }
}