package com.mercadolivro.controllers.response

import com.mercadolivro.enums.CustomerStatus

data class CustomerResponse (
  val id: Int? = null,
  val name: String,
  val email: String,
  val status: CustomerStatus
)
