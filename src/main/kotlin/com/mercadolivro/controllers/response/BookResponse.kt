package com.mercadolivro.controllers.response

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.models.CustomerModel
import java.math.BigDecimal

data class BookResponse(
  val id: Int? = null,
  val name: String,
  val price: BigDecimal,
  val customer: CustomerModel? = null,
  val status: BookStatus? = null
)
