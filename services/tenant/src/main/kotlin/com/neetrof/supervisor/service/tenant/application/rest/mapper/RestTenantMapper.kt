package com.neetrof.supervisor.service.tenant.application.rest.mapper

import com.neetrof.supervisor.service.tenant.application.rest.request.CreateTenantRequest
import com.neetrof.supervisor.service.tenant.application.rest.request.UpdateTenantRequest
import com.neetrof.supervisor.service.tenant.application.rest.response.TenantResponse
import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.Tenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant
import org.springframework.stereotype.Service

@Service
class RestTenantMapper {
    fun toResponse(tenant: Tenant): TenantResponse {
        return TenantResponse(
            id = tenant.id,
            name = tenant.name,
        )
    }

    fun toDomain(entity: CreateTenantRequest): CreateTenant {
        return CreateTenant(
            id = entity.id,
            name = entity.name,
        )
    }

    fun toDomain(entity: UpdateTenantRequest): UpdateTenant {
        return UpdateTenant(
            id = entity.id,
            name = entity.name,
        )
    }
}