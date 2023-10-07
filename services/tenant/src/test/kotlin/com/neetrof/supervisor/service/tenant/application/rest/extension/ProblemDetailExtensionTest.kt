package com.neetrof.supervisor.service.tenant.application.rest.extension

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.ProblemDetail

class ProblemDetailExtensionTest {

    @Test
    fun `add stack trace to ProblemDetail`() {
        //Given
        val problemDetail = ProblemDetail.forStatus(200)
        val exception = Exception("Fake exception")

        // When
        val result = problemDetail.addStackTrace(exception)

        // Then
        Assertions.assertThat(result.detail).isNotBlank()
        Assertions.assertThat(result.detail).contains("ProblemDetailExtensionTest")
    }
}