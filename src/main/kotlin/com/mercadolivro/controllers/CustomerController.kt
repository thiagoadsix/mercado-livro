package com.mercadolivro.controllers

import com.mercadolivro.controllers.request.PostCustomerRequest
import com.mercadolivro.controllers.request.PutCustomerRequest
import com.mercadolivro.controllers.response.CustomerResponse
import com.mercadolivro.extensions.toCustomerModel
import com.mercadolivro.extensions.toResponse
import com.mercadolivro.services.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customer")
class CustomerController(
  val customerService: CustomerService
) {

  @GetMapping
  fun getAll(@RequestParam name: String?): List<CustomerResponse> {
    return customerService.getAll(name).map { it.toResponse() }
  }

  @GetMapping("/{id}")
  fun get(@PathVariable id: Int): CustomerResponse {
    return customerService.findById(id).toResponse()
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody @Valid customer: PostCustomerRequest) {
    customerService.create(customer.toCustomerModel())
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
    val customerSaved = customerService.findById(id)
    customerService.update(customer.toCustomerModel(customerSaved))
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Int) {
    customerService.delete(id)
  }
}
