package malewicz.jakub.notification_service.services

import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailService(
    private val templateService: TemplateService,
    private val mailSender: JavaMailSender,
    @Value("\${gymapp.email}") private val email: String,
) {
    fun send(templateName: String, receiver: String, subject: String, contextVariables: Map<String, Any> = HashMap()) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(mimeMessage)

        mimeMessageHelper.setFrom(email)
        mimeMessageHelper.setTo(receiver)
        mimeMessageHelper.setSubject(subject)
        mimeMessageHelper.setText(templateService.getTemplateContent(templateName, contextVariables), true)

        mailSender.send(mimeMessage)
    }
}