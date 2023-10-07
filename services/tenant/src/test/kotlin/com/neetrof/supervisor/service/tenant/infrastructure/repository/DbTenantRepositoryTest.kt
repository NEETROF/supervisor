package com.neetrof.supervisor.service.tenant.infrastructure.repository

import com.neetrof.supervisor.service.tenant.DataSourceInitializer
import com.neetrof.supervisor.service.tenant.domain.service.model.aCreateTenant
import com.neetrof.supervisor.service.tenant.domain.service.model.aTenant
import com.neetrof.supervisor.service.tenant.domain.service.model.aUpdateTenant
import com.neetrof.supervisor.service.tenant.infrastructure.mapper.TenantMapper
import com.neetrof.supervisor.service.tenant.infrastructure.repository.entity.aTenantEntity
import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
@ExtendWith(MockKExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [DataSourceInitializer::class])
class DbTenantRepositoryTest {

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()

        jpaTenantRepository.deleteAll()
    }

    @MockkBean
    lateinit var tenantMapper: TenantMapper

    @Autowired
    lateinit var jpaTenantRepository: JpaTenantRepository

    @Autowired
    lateinit var dbTenantRepository: DbTenantRepository

    @Test
    fun `Get tenant by id`() {
        // add a tenant to the database
        jpaTenantRepository.save(aTenantEntity())

        //Given
        val tenant = aTenant()
        val tenantEntity = aTenantEntity()
        val tenantId = tenantEntity.id

        every { tenantMapper.toDomain(tenantEntity) } returns tenant

        // When
        val result = dbTenantRepository.getTenantById(tenantId)

        // Then
        verify(exactly = 1) { tenantMapper.toDomain(tenantEntity) }

        assertThat(result).isEqualTo(tenant)
    }

    @Test
    fun `Get tenants`() {
        // add a tenant to the database
        jpaTenantRepository.save(aTenantEntity())

        //Given
        val tenant = aTenant()
        val tenantEntity = aTenantEntity()

        every { tenantMapper.toDomain(tenantEntity) } returns tenant

        // When
        val result = dbTenantRepository.getTenants()

        // Then
        verify(exactly = 1) { tenantMapper.toDomain(tenantEntity) }

        assertThat(result).containsExactly(tenant)
    }

    @Test
    fun `Create tenant`() {
        //Given
        val tenant = aTenant()
        val createTenant = aCreateTenant()
        val tenantEntity = aTenantEntity()

        every { tenantMapper.toDomain(tenantEntity) } returns tenant
        every { tenantMapper.toEntity(createTenant) } returns tenantEntity

        // When
        val result = dbTenantRepository.createTenant(createTenant)

        // Then
        verify(exactly = 1) { tenantMapper.toDomain(tenantEntity) }
        verify(exactly = 1) { tenantMapper.toEntity(createTenant) }

        assertThat(result).isEqualTo(tenant)
    }

    @Test
    fun `Create tenant failed if tenant already exists`() {
        // add a tenant to the database
        jpaTenantRepository.save(aTenantEntity())

        //Given
        val createTenant = aCreateTenant()

        // Call the createTenant function and verify that it throws an IllegalArgumentException
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            dbTenantRepository.createTenant(createTenant)
        }
    }

    @Test
    fun `Update tenant`() {
        // add a tenant to the database
        jpaTenantRepository.save(aTenantEntity())

        //Given
        val tenant = aTenant()
        val updateTenant = aUpdateTenant()
        val tenantEntity = aTenantEntity()

        every { tenantMapper.toDomain(tenantEntity) } returns tenant
        every { tenantMapper.toEntity(tenantEntity, updateTenant) } returns tenantEntity

        // When
        val result = dbTenantRepository.updateTenant(updateTenant)

        // Then
        verify(exactly = 1) { tenantMapper.toDomain(tenantEntity) }
        verify(exactly = 1) { tenantMapper.toEntity(tenantEntity, updateTenant) }

        assertThat(result).isEqualTo(tenant)
    }

    @Test
    fun `Delete tenant`() {
        //Given
        val tenant = aTenant()

        // When & Then
        dbTenantRepository.deleteTenant(tenant.id)
    }
}