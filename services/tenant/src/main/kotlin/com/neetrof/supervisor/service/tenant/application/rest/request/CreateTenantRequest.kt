package com.neetrof.supervisor.service.tenant.application.rest.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateTenantRequest(
    @field:NotNull
    @field:NotBlank
    @get:Size(min = 1, max = 40)
    val id: String,

    @field:NotNull
    @field:NotBlank
    @get:Size(min = 1, max = 255)
    val name: String,
)
