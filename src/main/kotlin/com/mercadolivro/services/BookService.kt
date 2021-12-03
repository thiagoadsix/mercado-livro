package com.mercadolivro.services

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.models.BookModel
import com.mercadolivro.models.CustomerModel
import com.mercadolivro.repositories.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
  val bookRepository: BookRepository
) {
  fun create(book: BookModel) {
    bookRepository.save(book)
  }

  fun findAll(pageable: Pageable): Page<BookModel> {
    return bookRepository.findAll(pageable)
  }

  fun findActives(pageable: Pageable): Page<BookModel> {
    return bookRepository.findByStatus(BookStatus.ACTIVE, pageable)
  }

  fun findById(id: Int): BookModel {
    return bookRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML101.message.format(id), Errors.ML101.code) }
  }

  fun delete(id: Int) {
    val book = findById(id)
    book.status = BookStatus.CANCELED
    update(book)
  }

  fun update(book: BookModel) {
    bookRepository.save(book)
  }

  fun deleteByCustomer(customer: CustomerModel) {
    val books = bookRepository.findByCustomer(customer)
    for (book in books) {
      book.status = BookStatus.DELETED
    }
    bookRepository.saveAll(books)
  }
}
