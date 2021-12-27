package com.mercadolivro.controllers.request

import com.mercadolivro.validations.EmailAvailable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest(
  @field:NotEmpty(message = "Nome deve ser informado.")
  var name: String,

  @field:NotEmpty(message = "E-mail deve ser informado.")
  @field:Email(message = "E-mail deve ser válido.")
  @EmailAvailable
  var email: String,

  @field:NotEmpty(message = "Password deve ser informado")
  var password: String
)
