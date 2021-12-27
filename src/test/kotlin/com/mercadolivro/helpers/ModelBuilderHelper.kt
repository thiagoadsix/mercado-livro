package com.mercadolivro.helpers

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import com.mercadolivro.models.BookModel
import com.mercadolivro.models.CustomerModel
import com.mercadolivro.models.PurchaseModel
import java.math.BigDecimal
import java.util.*

fun buildCustomer(
  id: Int? = null,
  name: String = "Customer Name",
  email: String = "${UUID.randomUUID()}@email.com",
  password: String = "1234567890"
) = CustomerModel(
  id = id,
  name = name,
  email = email,
  password = password,
  status = CustomerStatus.ACTIVE,
  roles = setOf(Role.CUSTOMER)
)

fun buildPurchase(
  id: Int? = null,
  customer: CustomerModel = buildCustomer(),
  books: MutableList<BookModel> = mutableListOf(),
  nfe: String? = UUID.randomUUID().toString(),
  price: BigDecimal = BigDecimal.TEN,
) = PurchaseModel(
  id = id,
  customer = customer,
  books = books,
  nfe = nfe,
  price = price
)