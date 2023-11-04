package com.neetrof.supervisor.service.tenant.infrastructure.repository

import com.neetrof.supervisor.service.tenant.infrastructure.entity.TenantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaTenantRepository : JpaRepository<TenantEntity, String>