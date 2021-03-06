package com.mercadolivro.controllers.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PutCustomerRequest(
  @field:NotEmpty(message = "Nome deve ser informado.")
  var name: String,

  @field:Email(message = "E-mail deve ser válido.")
  var email: String,

  @field:NotEmpty(message = "Password deve ser informada.")
  var password: String
)
