package com.neetrof.supervisor.service.tenant.application.rest.extension

import org.springframework.http.ProblemDetail

fun ProblemDetail.addStackTrace(ex: Exception) : ProblemDetail {
    val stackTrace = StringBuilder()
    for (element in ex.stackTrace) {
        stackTrace.append(element.toString())
        stackTrace.append("\n")
    }
    this.detail = stackTrace.toString()

    return this
}