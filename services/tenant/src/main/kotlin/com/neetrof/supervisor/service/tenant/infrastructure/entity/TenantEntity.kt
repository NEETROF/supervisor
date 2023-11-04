package com.neetrof.supervisor.service.tenant.infrastructure.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "tenant")
@Entity
data class TenantEntity(
    @Id
    @Column(nullable = false)
    val id: String,

    @Column(nullable = false)
    val name: String,
){
    constructor() : this("", "")
}