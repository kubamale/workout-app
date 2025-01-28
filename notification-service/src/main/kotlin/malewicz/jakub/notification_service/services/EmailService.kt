package malewicz.jakub.notification_service.services

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailService(
    private val templateService: TemplateService,
    private val mailSender: JavaMailSender,
) {
    fun send(templateName: String, receiver: String, contextVariables: Map<String, Any> = HashMap()) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(mimeMessage)

        mimeMessageHelper.setFrom("kubamalewicztest@gmail.com")
        mimeMessageHelper.setTo(receiver)
        mimeMessageHelper.setSubject("test email")
        mimeMessageHelper.setText(templateService.getTemplateContent(templateName, contextVariables), true)

        mailSender.send(mimeMessage)
    }
}