package com.neetrof.supervisor.service.tenant.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.neetrof.supervisor.service.tenant.application.rest.TenantController
import com.neetrof.supervisor.service.tenant.application.rest.mapper.RestTenantMapper
import com.neetrof.supervisor.service.tenant.application.rest.request.CreateTenantRequest
import com.neetrof.supervisor.service.tenant.application.rest.request.UpdateTenantRequest
import com.neetrof.supervisor.service.tenant.application.rest.response.TenantResponse
import com.neetrof.supervisor.service.tenant.domain.model.CreateTenant
import com.neetrof.supervisor.service.tenant.domain.model.UpdateTenant
import com.neetrof.supervisor.service.tenant.domain.port.driving.TenantDrivingPort
import com.neetrof.supervisor.service.tenant.domain.service.model.aTenant
import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TenantController::class])
class TenantControllerTest(@Autowired val mockMvc: MockMvc) {

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @MockkBean
    lateinit var tenantDrivingPort: TenantDrivingPort

    @MockkBean
    lateinit var restTenantMapper: RestTenantMapper

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Test
    fun `Get tenant by id`() {
        //Given
        val tenant = aTenant()
        val tenantResponse = TenantResponse(tenant.id, tenant.name)

        every { tenantDrivingPort.getTenantById(tenant.id) } returns aTenant()
        every { restTenantMapper.toResponse(tenant) } returns tenantResponse

        // When
        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/{tenantId}", tenant.id)
        ).andDo(
            MockMvcResultHandlers.print()
        ).andExpect(
            MockMvcResultMatchers.status().`is`(HttpStatus.OK.value())
        ).andReturn()

        val responseBody = result.response.contentAsString
        val responseDto: TenantResponse = objectMapper.readValue(responseBody)
        assertThat(responseDto).isEqualTo(tenantResponse)
    }

    @Test
    fun `Get tenant by id with NOT_FOUND status`() {
        //Given
        val tenantId = "unknown_tenant"

        every { tenantDrivingPort.getTenantById(tenantId) } returns null

        // When
        mockMvc.perform(
            MockMvcRequestBuilders.get("/{tenantId}", tenantId)
        ).andDo(
            MockMvcResultHandlers.print()
        ).andExpect(
            MockMvcResultMatchers.status().`is`(HttpStatus.NOT_FOUND.value())
        )

        verify(exactly = 1) { tenantDrivingPort.getTenantById(tenantId) }
    }

    @Test
    fun `Get tenants`() {
        //Given
        val tenant = aTenant()
        val tenantResponse = TenantResponse(tenant.id, tenant.name)

        every { tenantDrivingPort.getTenants() } returns listOf(tenant)
        every { restTenantMapper.toResponse(tenant) } returns tenantResponse

        // When
        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/")
        ).andDo(
            MockMvcResultHandlers.print()
        ).andExpect(
            MockMvcResultMatchers.status().`is`(HttpStatus.OK.value())
        ).andReturn()

        verify(exactly = 1) { tenantDrivingPort.getTenants() }
        verify(exactly = 1) { restTenantMapper.toResponse(tenant) }

        val responseBody = result.response.contentAsString

        val responseDto: List<TenantResponse> =
            objectMapper.readValue(responseBody)

        assertThat(responseDto).isEqualTo(listOf(tenantResponse))
    }

    @Test
    fun `Create tenant`() {
        //Given
        val tenant = aTenant()
        val tenantId = tenant.id

        val createTenantRequest = CreateTenantRequest(
            tenantId,
            tenant.name
        )
        val createTenant = CreateTenant(
            tenantId,
            tenant.name
        )

        val tenantResponse = TenantResponse(tenant.id, tenant.name)

        every { restTenantMapper.toDomain(createTenantRequest) } returns createTenant
        every { restTenantMapper.toResponse(tenant) } returns tenantResponse
        every { tenantDrivingPort.createTenant(createTenant) } returns tenant

        // When
        val json: String = objectMapper.writeValueAsString(createTenantRequest)
        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.status().`is`(HttpStatus.CREATED.value())
            )
            .andReturn()


        verify(exactly = 1) { restTenantMapper.toDomain(createTenantRequest) }
        verify(exactly = 1) { restTenantMapper.toResponse(tenant) }
        verify(exactly = 1) { tenantDrivingPort.createTenant(createTenant) }

        val responseBody = result.response.contentAsString
        val responseDto: TenantResponse = objectMapper.readValue(responseBody)
        assertThat(responseDto).isEqualTo(tenantResponse)
    }

    @Test
    fun `Update tenant`() {
        //Given
        val tenant = aTenant()
        val tenantId = tenant.id

        val updateTenantRequest = UpdateTenantRequest(
            tenantId,
            tenant.name
        )
        val updateTenant = UpdateTenant(
            tenantId,
            tenant.name
        )

        val tenantResponse = TenantResponse(tenant.id, tenant.name)

        every { restTenantMapper.toDomain(updateTenantRequest) } returns updateTenant
        every { restTenantMapper.toResponse(tenant) } returns tenantResponse
        every { tenantDrivingPort.updateTenant(updateTenant) } returns tenant

        // When
        val json: String = objectMapper.writeValueAsString(updateTenantRequest)
        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.put("/{tenantId}", tenantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.status().`is`(HttpStatus.OK.value())
            )
            .andReturn()

        verify(exactly = 1) { restTenantMapper.toDomain(updateTenantRequest) }
        verify(exactly = 1) { restTenantMapper.toResponse(tenant) }
        verify(exactly = 1) { tenantDrivingPort.updateTenant(updateTenant) }

        val responseBody = result.response.contentAsString
        val responseDto: TenantResponse = objectMapper.readValue(responseBody)
        assertThat(responseDto).isEqualTo(tenantResponse)
    }

    @Test
    fun `Update tenant Not Found`() {
        //Given
        val tenant = aTenant()
        val tenantId = tenant.id

        val updateTenantRequest = UpdateTenantRequest(
            tenantId,
            tenant.name
        )

        every { tenantDrivingPort.updateTenant(restTenantMapper.toDomain(updateTenantRequest)) } returns null

        // When
        val json: String = objectMapper.writeValueAsString(updateTenantRequest)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/{tenantId}", tenantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.status().`is`(HttpStatus.NOT_FOUND.value())
            )
    }

    @Test
    fun `Delete tenant`() {
        //Given
        val tenant = aTenant()
        val tenantId = tenant.id

        every { tenantDrivingPort.deleteTenant(tenantId) } returns Unit

        // When
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/{tenantId}", tenantId)
        ).andDo(
            MockMvcResultHandlers.print()
        ).andExpect(
            MockMvcResultMatchers.status().`is`(HttpStatus.NO_CONTENT.value())
        )
    }
}