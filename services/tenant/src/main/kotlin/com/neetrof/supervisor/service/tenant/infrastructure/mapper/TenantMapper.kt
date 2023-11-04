package com.neetrof.supervisor.service.tenant.infrastructure.mapper

import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.Tenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant
import com.neetrof.supervisor.service.tenant.infrastructure.entity.TenantEntity
import org.springframework.stereotype.Service

@Service
class TenantMapper {
    fun toEntity(createTenant: CreateTenant): TenantEntity {
        return TenantEntity(
            id = createTenant.id,
            name = createTenant.name,
        )
    }

    fun toEntity(originalEntity: TenantEntity, updateTenant: UpdateTenant): TenantEntity {
        return originalEntity.copy(
            id = originalEntity.id,
            name = updateTenant.name ?: originalEntity.name,
        )
    }

    fun toDomain(entity: TenantEntity): Tenant {
        return Tenant(
            id = entity.id,
            name = entity.name,
        )
    }
}