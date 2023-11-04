package com.neetrof.supervisor.service.tenant.application.rest.handler

import com.neetrof.supervisor.service.tenant.application.rest.extension.addStackTrace
import com.neetrof.supervisor.service.tenant.application.rest.extension.getMessage
import com.neetrof.supervisor.service.tenant.domain.exception.AlreadyExistsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.http.*
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @Value("\${spring.profiles.active:}")
    private val activeProfile: String? = null

    @Autowired
    private lateinit var messageSource: MessageSource

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val problemDetail = ProblemDetail.forStatus(ex.statusCode)

        val sbError = StringBuffer()
        sbError.appendLine(messageSource.getMessage("invalid_request_content"))
        for (error in ex.bindingResult.fieldErrors) {
            sbError.appendLine("${messageSource.getMessage("field")} '${error.field}': ${error.defaultMessage?:""}.")
        }

        problemDetail.title = messageSource.getMessage("bad_request")
        problemDetail.detail = sbError.toString()

        return handleExceptionInternal(
            ex, problemDetail,
            HttpHeaders(), ex.statusCode, request
        )
    }

    @ExceptionHandler(value = [AlreadyExistsException::class])
    protected fun handleAlreadyExists(
        ex: RuntimeException, request: WebRequest
    ): ResponseEntity<Any>? {
        val problemDetail = ProblemDetail.forStatus(
            HttpStatus.FORBIDDEN,
        )
        problemDetail.title = ex.message ?: "The entity already exist"

        if(request is ServletWebRequest) problemDetail.instance = URI.create (request.request.requestURI)

        if (activeProfile == "dev") problemDetail.addStackTrace(ex)

        return handleExceptionInternal(
            ex, problemDetail,
            HttpHeaders(), HttpStatus.FORBIDDEN, request
        )
    }
}