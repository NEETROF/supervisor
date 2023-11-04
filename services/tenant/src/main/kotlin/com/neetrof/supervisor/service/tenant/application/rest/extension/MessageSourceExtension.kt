package com.neetrof.supervisor.service.tenant.application.rest.extension

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

fun MessageSource.getMessage(key: String, args: Array<*> ?= null) : String {
    return this.getMessage(key, args, null, LocaleContextHolder.getLocale()) ?: key
}