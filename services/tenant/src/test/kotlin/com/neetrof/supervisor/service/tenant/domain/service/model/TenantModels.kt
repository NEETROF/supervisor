package com.neetrof.supervisor.service.tenant.domain.service.model

import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.Tenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant

fun aTenant() : Tenant {
    return Tenant("tenant_1", "tenant 1")
}

fun aCreateTenant() : CreateTenant {
    return CreateTenant("tenant_1", "tenant 1")
}

fun aUpdateTenant() : UpdateTenant {
    return UpdateTenant("tenant_1", "tenant 1")
}