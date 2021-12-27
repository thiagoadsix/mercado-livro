package com.mercadolivro.models

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import javax.persistence.*

@Entity(name = "customer")
data class CustomerModel(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Int? = null,

  @Column
  var name: String,

  @Column
  var email: String,

  @Column
  var password: String,

  @Column
  @Enumerated(EnumType.STRING)
  var status: CustomerStatus,

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
  @CollectionTable(
    name = "customer_role",
    joinColumns = [JoinColumn(name = "customer_id")]
  )
  var roles: Set<Role> = setOf()
)
