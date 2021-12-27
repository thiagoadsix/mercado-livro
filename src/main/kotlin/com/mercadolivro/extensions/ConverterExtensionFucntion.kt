package com.mercadolivro.extensions

import com.mercadolivro.controllers.request.PostBookRequest
import com.mercadolivro.controllers.request.PostCustomerRequest
import com.mercadolivro.controllers.request.PutBookRequest
import com.mercadolivro.controllers.request.PutCustomerRequest
import com.mercadolivro.controllers.response.BookResponse
import com.mercadolivro.controllers.response.CustomerResponse
import com.mercadolivro.controllers.response.PageResponse
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.models.BookModel
import com.mercadolivro.models.CustomerModel
import org.springframework.data.domain.Page

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
  return CustomerModel(name = this.name, email = this.email, status = CustomerStatus.ACTIVE, password = this.password)
}

fun PutCustomerRequest.toCustomerModel(customer: CustomerModel): CustomerModel {
  return CustomerModel(
    id = customer.id,
    name = this.name,
    email = this.email,
    status = customer.status,
    password = customer.password
  )
}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
  return BookModel(id = null, name = this.name, price = this.price, status = BookStatus.ACTIVE, customer = customer)
}

fun PutBookRequest.toBookModel(book: BookModel): BookModel {
  return BookModel(
    id = book.id,
    name = this.name ?: book.name,
    price = this.price ?: book.price,
    status = book.status,
    customer = book.customer
  )
}

fun CustomerModel.toResponse(): CustomerResponse {
  return CustomerResponse(
    id = this.id,
    name = this.name,
    email = this.email,
    status = this.status
  )
}

fun BookModel.toResponse(): BookResponse {
  return BookResponse(
    id = this.id,
    name = this.name,
    price = this.price,
    customer = this.customer,
    status = this.status
  )
}

fun <T> Page<T>.toPageResponse(): PageResponse<T> {
  return PageResponse(
    items = this.content,
    currentPage = this.number,
    totalItems = this.totalElements,
    totalPages = this.totalPages
  )
}