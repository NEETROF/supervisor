package com.neetrof.supervisor.service.tenant.infrastructure.mapper

import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant
import com.neetrof.supervisor.service.tenant.infrastructure.repository.entity.aTenantEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TenantMapperTest {

    private val tenantMapper = TenantMapper()

    @Test
    fun `Transform CreateTenant to TenantEntity`() {
        //Given
        val createTenant = CreateTenant(
            id = "id",
            name = "name",
        )

        // When
        val result = tenantMapper.toEntity(createTenant)

        // Then
        assertThat(result.id).isEqualTo(createTenant.id)
        assertThat(result.name).isEqualTo(createTenant.name)
    }

    @Test
    fun `Transform UpdateTenant to TenantEntity`() {
        //Given
        val  tenantEntity = aTenantEntity()

        val updateTenant = UpdateTenant(
            id = "update_id",
            name = "update_name",
        )

        // When
        val result = tenantMapper.toEntity(tenantEntity, updateTenant)

        // Then
        assertThat(tenantEntity).isEqualTo(aTenantEntity()).describedAs("Original entity should not be updated")

        assertThat(result.id).isEqualTo(aTenantEntity().id).describedAs("Id should not be updated and should be the same as original entity id")
        assertThat(result.name).isEqualTo(updateTenant.name)
    }

    @Test
    fun `Transform partial UpdateTenant to TenantEntity`() {
        //Given
        val  tenantEntity = aTenantEntity()

        val updateTenant = UpdateTenant(
            id = "update_id"
        )

        // When
        val result = tenantMapper.toEntity(tenantEntity, updateTenant)

        // Then
        assertThat(tenantEntity).isEqualTo(aTenantEntity()).describedAs("Original entity should not be updated")

        assertThat(result.id).isEqualTo(aTenantEntity().id).describedAs("Id should not be updated and should be the same as original entity id")
        assertThat(result.name).isEqualTo(aTenantEntity().name)
    }

    @Test
    fun `Transform EntityTenant to Entity`() {
        //Given
        val  tenantEntity = aTenantEntity()

        // When
        val result = tenantMapper.toDomain(tenantEntity)

        // Then
        assertThat(tenantEntity).isEqualTo(aTenantEntity()).describedAs("Original entity should not be updated")

        assertThat(result.id).isEqualTo(tenantEntity.id)
        assertThat(result.name).isEqualTo(tenantEntity.name)
    }
}