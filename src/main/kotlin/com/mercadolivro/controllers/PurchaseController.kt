package com.mercadolivro.controllers

import com.mercadolivro.controllers.mappers.PurchaseMapper
import com.mercadolivro.controllers.request.PostPurchaseRequest
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exceptions.BadRequestException
import com.mercadolivro.services.BookService
import com.mercadolivro.services.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("purchases")
class PurchaseController(
  private val purchaseService: PurchaseService,
  private val purchaseMapper: PurchaseMapper,
  private val bookService: BookService
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun purchase(@RequestBody request: PostPurchaseRequest) {
    request.bookIds.map {
      val book = bookService.findById(it)
      if (book.status == BookStatus.DELETED || book.status == BookStatus.CANCELED) {
        throw BadRequestException(Errors.ML202.message, Errors.ML202.code)
      }
    }
    purchaseService.create(purchaseMapper.toModel(request))
  }
}