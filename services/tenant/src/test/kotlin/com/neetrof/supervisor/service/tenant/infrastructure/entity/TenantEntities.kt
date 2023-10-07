package com.neetrof.supervisor.service.tenant.infrastructure.repository.entity

import com.neetrof.supervisor.service.tenant.infrastructure.entity.TenantEntity

fun aTenantEntity() : TenantEntity {
    return TenantEntity().copy(id = "tenant_1", name = "tenant 1")
}
