package com.mercadolivro.controllers.mappers

import com.mercadolivro.controllers.request.PostPurchaseRequest
import com.mercadolivro.models.PurchaseModel
import com.mercadolivro.services.BookService
import com.mercadolivro.services.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
  private val bookService: BookService,
  private val customerService: CustomerService
) {
  fun toModel(request: PostPurchaseRequest): PurchaseModel {
    val customer = customerService.findById(request.customerId)
    val books = bookService.findAllByIds(request.bookIds)

    return PurchaseModel(
      customer = customer,
      books = books,
      price = books.sumOf { it.price }
    )
  }
}