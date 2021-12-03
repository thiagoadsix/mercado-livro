package com.mercadolivro.repositories

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.models.BookModel
import com.mercadolivro.models.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface BookRepository : JpaRepository<BookModel, Int> {
  fun findByStatus(status: BookStatus, pageable: Pageable): Page<BookModel>
  fun findByCustomer(customer: CustomerModel): List<BookModel>
}