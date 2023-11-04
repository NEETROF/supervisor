package com.neetrof.supervisor.service.tenant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*


@SpringBootApplication
class TenantApplication{
    @Bean
    fun localeResolver(): LocaleResolver {
        val slr = AcceptHeaderLocaleResolver()
        slr.setDefaultLocale(Locale.US)
        slr.supportedLocales = listOf(Locale.US, Locale.FRENCH)
        return slr
    }
}

fun main(args: Array<String>) {
    runApplication<TenantApplication>(*args)
}