package com.neetrof.supervisor.service.tenant.application.rest

import com.neetrof.supervisor.service.tenant.application.rest.mapper.RestTenantMapper
import com.neetrof.supervisor.service.tenant.application.rest.request.CreateTenantRequest
import com.neetrof.supervisor.service.tenant.application.rest.request.UpdateTenantRequest
import com.neetrof.supervisor.service.tenant.application.rest.response.TenantResponse
import com.neetrof.supervisor.service.tenant.domain.port.driving.TenantDrivingPort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Tenant", description = "Tenant management APIs")
@RestController
class TenantController(
    val tenantDrivingPort: TenantDrivingPort,
    val restTenantMapper: RestTenantMapper,
) {
    @Operation(summary = "Get a tenant by id", description = "Return a tenant as per the id")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        ApiResponse(responseCode = "404", description = "The tenant was not found"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    )
    @GetMapping("/{id}")
    fun getTenantById(@PathVariable id: String): ResponseEntity<TenantResponse?> {
        tenantDrivingPort.getTenantById(id)?.let {
            return ResponseEntity(restTenantMapper.toResponse(it), HttpStatus.OK)
        } ?: return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @Operation(summary = "Get tenants", description = "Return tenant list")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    )
    @GetMapping
    fun getTenants(): ResponseEntity<List<TenantResponse>> {
        val tenantResponseList = tenantDrivingPort.getTenants().map {
            restTenantMapper.toResponse(it)
        }

        return ResponseEntity(tenantResponseList, HttpStatus.OK)
    }

    @PostMapping
    @Operation(summary = "Create a tenant by id", description = "Return the created tenant")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Successfully created"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    )
    fun createTenant(@Valid @RequestBody createTenantRequest: CreateTenantRequest): ResponseEntity<TenantResponse> {
        val tenant = tenantDrivingPort.createTenant(restTenantMapper.toDomain(createTenantRequest))
        return ResponseEntity(
            restTenantMapper.toResponse(tenant),
            HttpStatus.CREATED
        )
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tenant by id", description = "Return the updated tenant")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully updated"),
        ApiResponse(responseCode = "404", description = "The tenant was not found"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    )
    fun updateTenant(
        @PathVariable id: String,
        @Valid @RequestBody updateTenantRequest: UpdateTenantRequest
    ): ResponseEntity<TenantResponse> {
        return tenantDrivingPort.updateTenant(
            restTenantMapper.toDomain(
                updateTenantRequest.copy(id = id)
            )
        )?.let {
            ResponseEntity(restTenantMapper.toResponse(it), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tenant by id", description = "Delete a tenant as per the id")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Action performed"),
        ApiResponse(responseCode = "500", description = "Internal Server Error")
    )
    fun deleteTenant(@PathVariable id: String): ResponseEntity<Unit> {
        tenantDrivingPort.deleteTenant(id)
        return ResponseEntity(
            HttpStatus.NO_CONTENT
        )
    }
}