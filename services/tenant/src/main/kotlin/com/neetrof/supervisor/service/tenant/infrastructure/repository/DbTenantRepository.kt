package com.neetrof.supervisor.service.tenant.infrastructure.repository

import com.neetrof.supervisor.service.tenant.application.rest.extension.getMessage
import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.Tenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant
import com.neetrof.supervisor.service.tenant.domain.port.driven.TenantDrivenPort
import com.neetrof.supervisor.service.tenant.infrastructure.extension.findOne
import com.neetrof.supervisor.service.tenant.infrastructure.mapper.TenantMapper
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class DbTenantRepository (
    val jpaTenantRepository: JpaTenantRepository,
    val tenantMapper: TenantMapper,
    val messageSource: MessageSource
) : TenantDrivenPort {
    override fun getTenantById(id: String): Tenant? {
        return jpaTenantRepository.findOne(id)?.let {
            tenantMapper.toDomain(it)
        }
    }

    override fun getTenants(): List<Tenant> {
        return jpaTenantRepository.findAll()
            .map { tenantMapper.toDomain(it) }
    }

    override fun createTenant(createTenant: CreateTenant): Tenant {

        require(jpaTenantRepository.findOne(createTenant.id) == null,
            lazyMessage = { messageSource.getMessage("tenant_with_id_p0_already_exists", arrayOf(createTenant.id))
        })

        var tenantEntity = tenantMapper.toEntity(createTenant)
        tenantEntity = jpaTenantRepository.save(tenantEntity)
        return tenantMapper.toDomain(tenantEntity)
    }

    override fun updateTenant(updateTenant: UpdateTenant): Tenant? {
        return jpaTenantRepository.findOne(updateTenant.id)?.let { existingEntity ->
            val updatedEntity = tenantMapper.toEntity(existingEntity, updateTenant)
            return tenantMapper.toDomain(jpaTenantRepository.save(updatedEntity))
        }
    }

    override fun deleteTenant(id: String) {
        jpaTenantRepository.deleteById(id)
    }
}