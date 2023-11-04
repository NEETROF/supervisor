package com.neetrof.supervisor.service.tenant.domain.service

import com.neetrof.supervisor.service.tenant.domain.exception.AlreadyExistsException
import com.neetrof.supervisor.service.tenant.domain.port.driven.TenantDrivenPort
import com.neetrof.supervisor.service.tenant.domain.service.model.aCreateTenant
import com.neetrof.supervisor.service.tenant.domain.service.model.aTenant
import com.neetrof.supervisor.service.tenant.domain.service.model.aUpdateTenant
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.MessageSource

@ExtendWith(MockKExtension::class)
class TenantServiceTest {

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @MockK
    lateinit var tenantDrivenPort: TenantDrivenPort

    @MockK
    lateinit var messageSource: MessageSource

    @InjectMockKs
    lateinit var tenantService: TenantService

    @Test
    fun `Get tenant by id`() {
        //Given
        val tenant = aTenant()
        val tenantId = tenant.id

        every { tenantDrivenPort.getTenantById(tenantId) } returns tenant

        // When
        val result = tenantService.getTenantById(tenantId)

        // Then
        verify(exactly = 1) { tenantDrivenPort.getTenantById(tenantId) }

        assertThat(result).isEqualTo(tenant)
    }

    @Test
    fun `Get tenants`() {
        //Given
        val tenant = aTenant()
        every { tenantDrivenPort.getTenants() } returns listOf(tenant)

        // When
        val result = tenantService.getTenants()

        // Then
        verify(exactly = 1) { tenantDrivenPort.getTenants() }

        assertThat(result).containsExactly(tenant)
    }

    @Test
    fun `Create tenant`() {
        //Given
        val tenant = aTenant()
        val createTenant = aCreateTenant()

        every { tenantDrivenPort.getTenantById(tenant.id) } returns null
        every { tenantDrivenPort.createTenant(createTenant) } returns tenant

        // When
        val result = tenantService.createTenant(createTenant)

        // Then
        verify(exactly = 1) { tenantDrivenPort.createTenant(createTenant) }

        assertThat(result).isEqualTo(tenant)
    }

    @Test
    fun `Create tenant failed if tenant already exists`() {
        //Given
        val tenant = aTenant()
        val createTenant = aCreateTenant()

        every { tenantDrivenPort.getTenantById(tenant.id) } returns tenant
        every { tenantDrivenPort.createTenant(createTenant) } returns tenant
        every { messageSource.getMessage("tenant_with_id_p0_already_exists", any(), any(), any()) } returns "tenant already exists"

        // Call the createTenant function and verify that it throws an AlreadyExistsException
        val exception = Assertions.assertThrows(AlreadyExistsException::class.java) {
            tenantService.createTenant(createTenant)
        }

        // Then
        verify(exactly = 0) { tenantDrivenPort.createTenant(createTenant) }
        verify(exactly = 1) { messageSource.getMessage("tenant_with_id_p0_already_exists", any(), any(), any()) }

        assertThat(exception.message).isEqualTo("tenant already exists")
    }

    @Test
    fun `Update tenant`() {
        //Given
        val tenant = aTenant()
        val aUpdateTenant = aUpdateTenant()

        every { tenantDrivenPort.updateTenant(aUpdateTenant) } returns tenant

        // When
        val result = tenantService.updateTenant(aUpdateTenant)

        // Then
        assertThat(result).isEqualTo(tenant)
    }

    @Test
    fun `Delete tenant`() {
        //Given
        val tenant = aTenant()

        every { tenantDrivenPort.deleteTenant(tenant.id) } returns Unit

        // When
        tenantService.deleteTenant(tenant.id)

        // Then
        verify(exactly = 1) { tenantDrivenPort.deleteTenant(tenant.id) }
    }
}