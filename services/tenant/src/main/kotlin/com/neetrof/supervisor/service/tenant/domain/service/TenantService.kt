package com.neetrof.supervisor.service.tenant.domain.service

import com.neetrof.supervisor.service.tenant.application.rest.extension.getMessage
import com.neetrof.supervisor.service.tenant.domain.exception.AlreadyExistsException
import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.Tenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant
import com.neetrof.supervisor.service.tenant.domain.port.driven.TenantDrivenPort
import com.neetrof.supervisor.service.tenant.domain.port.driving.TenantDrivingPort
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class TenantService (
    val tenantDrivenPort: TenantDrivenPort,
    val messageSource: MessageSource
) : TenantDrivingPort {
    override fun getTenantById(id: String): Tenant? =
        tenantDrivenPort.getTenantById(id)

    override fun getTenants(): List<Tenant> =
        tenantDrivenPort.getTenants()

    override fun createTenant(createTenant: CreateTenant): Tenant {
        if(tenantDrivenPort.getTenantById(createTenant.id) != null)
            throw AlreadyExistsException(messageSource.getMessage("tenant_with_id_p0_already_exists", arrayOf(createTenant.id)))
        return tenantDrivenPort.createTenant(createTenant)
    }

    override fun updateTenant(updateTenant: UpdateTenant): Tenant? =
        tenantDrivenPort.updateTenant(updateTenant)

    override fun deleteTenant(id: String) =
        tenantDrivenPort.deleteTenant(id)
}