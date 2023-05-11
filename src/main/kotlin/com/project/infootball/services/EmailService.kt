package com.project.infootball.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService (
    private val javaMailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    private val emailAddressOfSender: String,
) {
    fun sendEmail(receiver: String, subject: String, text: String) {
        SimpleMailMessage().apply {
            setFrom(emailAddressOfSender)
            setTo(receiver)
            setBcc(emailAddressOfSender)
            setSubject(subject)
            setText(text)

            javaMailSender.send(this)
        }
    }
}