package com.mercadolivro.services

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.models.CustomerModel
import com.mercadolivro.repositories.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
  val customerRepository: CustomerRepository,
  val bookService: BookService
) {

  fun getAll(name: String?): List<CustomerModel> {
    name?.let {
      return customerRepository.findByNameContaining(name)
    }
    return customerRepository.findAll().toList()
  }

  fun findById(id: Int): CustomerModel {
    return customerRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code) }
  }

  fun create(customer: CustomerModel) {
    customerRepository.save((customer))
  }

  fun update(customer: CustomerModel) {
    if (!customerRepository.existsById(customer.id!!)) {
      throw Exception()
    }
    customerRepository.save(customer)
  }

  fun delete(id: Int) {
    val customer = findById(id)
    bookService.deleteByCustomer(customer)
    customer.status = CustomerStatus.DESACTIVATE
    customerRepository.save(customer)
  }
}