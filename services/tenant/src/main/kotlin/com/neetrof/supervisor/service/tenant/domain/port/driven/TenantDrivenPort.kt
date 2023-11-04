package com.neetrof.supervisor.service.tenant.domain.port.driven

import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.Tenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant

interface TenantDrivenPort {
    fun createTenant(createTenant: CreateTenant): Tenant
    fun getTenantById(id: String): Tenant?
    fun getTenants(): List<Tenant>
    fun updateTenant(updateTenant: UpdateTenant): Tenant?
    fun deleteTenant(id: String)
}