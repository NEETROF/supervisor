package com.neetrof.supervisor.service.tenant.application.rest.handler

import com.neetrof.supervisor.service.tenant.domain.exception.AlreadyExistsException
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.MessageSource
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindingResult
import org.springframework.validation.BindingResultUtils
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/*
@ControllerAdvice
class ExtendedRestResponseEntityExceptionHandler : RestResponseEntityExceptionHandler() {

    public override fun handleAlreadyExists(
        ex: RuntimeException, request: WebRequest
    ): ResponseEntity<Any>? {
        return super.handleAlreadyExists(ex, request)
    }
}*/

@ExtendWith(MockKExtension::class)
class RestResponseEntityExceptionHandlerTest {

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @InjectMockKs
    lateinit var restResponseEntityExceptionHandler: RestResponseEntityExceptionHandler

    @MockK
    lateinit var webRequest: WebRequest

    @MockK
    lateinit var messageSource: MessageSource

    @Test
    fun handleMethodArgumentNotValid() {
        //Given
        val method =
            ResponseEntityExceptionHandler::class.java.declaredMethods.find { it.name == "handleMethodArgumentNotValid" }
        val modelName = "test"
        val methodArgumentNotValidException = MethodArgumentNotValidException(
            MethodParameter(method!!, 0),
            BindingResultUtils.getBindingResult(
                mapOf<String, BindingResult>(
                    BindingResult.MODEL_KEY_PREFIX + modelName to BeanPropertyBindingResult("test", "test").apply {
                        addError(
                            FieldError("test", "test", "test")
                        )
                    }
                ), modelName
            )!!
        )

        every { messageSource.getMessage(any(), null, null, any()) } returnsArgument 0

        //When
        val result = restResponseEntityExceptionHandler.handleException(methodArgumentNotValidException, webRequest)

        //Then
        assertThat(result).isNotNull
        assertThat(result!!.statusCode.value()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(result.body).isExactlyInstanceOf(ProblemDetail::class.java)
        val problemDetail = result.body as ProblemDetail
        assertThat(problemDetail.title).isEqualTo("bad_request")
        assertThat(problemDetail.detail).isEqualTo("invalid_request_content\nfield 'test': test.\n")
    }

    @Test
    fun handleAlreadyExists() {
        //Given
        val alreadyExistsException = AlreadyExistsException("fake exception")

        every { messageSource.getMessage(any(), null, null, any()) } returnsArgument 0

        //When
        val method = restResponseEntityExceptionHandler.javaClass.getDeclaredMethod(
            "handleAlreadyExists",
            RuntimeException::class.java,
            WebRequest::class.java
        )
            .apply { isAccessible = true }

        val result = method.invoke(restResponseEntityExceptionHandler, alreadyExistsException, webRequest)

        val responsEntity = if(result is ResponseEntity<*>) result else null

        //Then
        assertThat(responsEntity).isNotNull
        assertThat(responsEntity!!.statusCode.value()).isEqualTo(HttpStatus.FORBIDDEN.value())
        assertThat(responsEntity.body).isExactlyInstanceOf(ProblemDetail::class.java)
        val problemDetail = responsEntity.body as ProblemDetail
        assertThat(problemDetail.title).isEqualTo("fake exception")
        assertThat(problemDetail.detail).isNull()
    }
}