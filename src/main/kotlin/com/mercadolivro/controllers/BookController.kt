package com.mercadolivro.controllers

import com.mercadolivro.controllers.request.PostBookRequest
import com.mercadolivro.controllers.request.PutBookRequest
import com.mercadolivro.controllers.response.BookResponse
import com.mercadolivro.extensions.toBookModel
import com.mercadolivro.extensions.toResponse
import com.mercadolivro.services.BookService
import com.mercadolivro.services.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("book")
class BookController(
  val bookService: BookService,
  val customerService: CustomerService
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody request: PostBookRequest) {
    val customer = customerService.findById(request.customerId)
    bookService.create(request.toBookModel(customer))
  }

  @GetMapping
  fun findAll(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> = bookService.findAll(pageable).map { it.toResponse() }

  @GetMapping("/active")
  fun findActives(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> = bookService.findActives(pageable).map { it.toResponse() }

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Int): BookResponse = bookService.findById(id).toResponse()

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Int) = bookService.delete(id)

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun update(@PathVariable id: Int, @RequestBody book: PutBookRequest) {
    val bookSaved = bookService.findById(id)
    bookService.update(book.toBookModel(bookSaved))
  }
}
