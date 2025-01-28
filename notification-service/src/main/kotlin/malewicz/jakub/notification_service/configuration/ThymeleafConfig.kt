package malewicz.jakub.notification_service.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver


@Configuration
class ThymeleafConfig {
    @Bean
    fun templateResolver(): ClassLoaderTemplateResolver {
        val resolver = ClassLoaderTemplateResolver()
        resolver.prefix = "templates/"
        resolver.isCacheable = false
        resolver.suffix = ".html"
        resolver.setTemplateMode("HTML")
        resolver.characterEncoding = "UTF-8"

        return resolver
    }

    @Bean
    fun templateEngine(): SpringTemplateEngine {
        val engine = SpringTemplateEngine()
        engine.setTemplateResolver(templateResolver())
        return engine
    }
}