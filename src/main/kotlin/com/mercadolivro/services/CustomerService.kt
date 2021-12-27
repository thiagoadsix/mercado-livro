package com.mercadolivro.services

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Role
import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.models.CustomerModel
import com.mercadolivro.repositories.CustomerRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
  private val customerRepository: CustomerRepository,
  private val bookService: BookService,
  private val bCrypt: BCryptPasswordEncoder
) {

  fun getAll(name: String?): List<CustomerModel> {
    name?.let {
      return customerRepository.findByNameContaining(name)
    }
    return customerRepository.findAll().toList()
  }

  fun findById(id: Int): CustomerModel {
    return customerRepository.findById(id)
      .orElseThrow { NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code) }
  }

  fun create(customer: CustomerModel) {
    val customerCopy = customer.copy(
      roles = setOf(Role.CUSTOMER),
      password = bCrypt.encode(customer.password)
    )
    customerRepository.save((customerCopy))
  }

  fun update(customer: CustomerModel) {
    if (! customerRepository.existsById(customer.id !!)) {
      throw NotFoundException(Errors.ML201.message.format(customer.id), Errors.ML201.code)
    }
    customerRepository.save(customer)
  }

  fun delete(id: Int) {
    val customer = findById(id)
    bookService.deleteByCustomer(customer)
    customer.status = CustomerStatus.DESACTIVATE
    customerRepository.save(customer)
  }

  fun emailAvailable(email: String): Boolean {
    return ! customerRepository.existsByEmail(email)
  }
}