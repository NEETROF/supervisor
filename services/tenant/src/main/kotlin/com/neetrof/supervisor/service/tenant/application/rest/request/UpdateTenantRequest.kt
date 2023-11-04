package com.neetrof.supervisor.service.tenant.application.rest.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateTenantRequest(
    @NotBlank
    @Size(min = 1, max = 40)
    val id: String,

    @NotBlank
    @Size(min = 1, max = 255)
    val name: String? = null,
)
