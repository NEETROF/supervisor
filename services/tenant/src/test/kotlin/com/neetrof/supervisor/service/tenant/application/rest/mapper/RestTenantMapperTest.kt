package com.neetrof.supervisor.service.tenant.application.rest.mapper

import com.neetrof.supervisor.service.tenant.application.rest.request.CreateTenantRequest
import com.neetrof.supervisor.service.tenant.application.rest.request.UpdateTenantRequest
import com.neetrof.supervisor.service.tenant.domain.service.model.aTenant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RestTenantMapperTest {

    private val restTenantMapper = RestTenantMapper()

    @Test
    fun `Transform Tenant to TenantResponse`() {
        //Given
        val tenant = aTenant()

        // When
        val result = restTenantMapper.toResponse(tenant)

        // Then
        assertThat(result.id).isEqualTo(tenant.id)
        assertThat(result.name).isEqualTo(tenant.name)
    }

    @Test
    fun `Transform CreateTenantRequest to CreateTenant`() {
        //Given
        val createTenantRequest = CreateTenantRequest(
            id = "id",
            name = "name",
        )

        // When
        val result = restTenantMapper.toDomain(createTenantRequest)

        // Then
        assertThat(result.id).isEqualTo(createTenantRequest.id)
        assertThat(result.name).isEqualTo(createTenantRequest.name)
    }

    @Test
    fun `Transform UpdateTenantRequest to UpdateTenant`() {
        //Given
        val updateTenantRequest = UpdateTenantRequest(
            id = "id",
            name = "name",
        )

        // When
        val result = restTenantMapper.toDomain(updateTenantRequest)

        // Then
        assertThat(result.id).isEqualTo(updateTenantRequest.id)
        assertThat(result.name).isEqualTo(updateTenantRequest.name)
    }
}