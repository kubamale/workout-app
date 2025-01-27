package malewicz.jakub.notification_service.services

import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class TemplateService(private val templateEngine: TemplateEngine) {

    fun getTemplateContent(templateName: String, contextVariables: Map<String, Any> = HashMap()): String {
        val context = Context()
        context.setVariables(contextVariables)
        return templateEngine.process(templateName, context)
    }
}