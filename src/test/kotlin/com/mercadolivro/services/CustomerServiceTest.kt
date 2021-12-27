package com.mercadolivro.services

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.helpers.buildCustomer
import com.mercadolivro.repositories.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
  @MockK
  private lateinit var customerRepository: CustomerRepository

  @MockK
  private lateinit var bookService: BookService

  @MockK
  private lateinit var bCrypt: BCryptPasswordEncoder

  @InjectMockKs
  @SpyK
  private lateinit var customerService: CustomerService

  @Test
  fun `should return all customers`() {
    val fakeCustomers = listOf(buildCustomer(), buildCustomer())

    every { customerRepository.findAll() } returns fakeCustomers

    val customers = customerService.getAll(null)

    assertEquals(fakeCustomers, customers)
    verify(exactly = 1) { customerRepository.findAll() }
    verify(exactly = 0) { customerRepository.findByNameContaining(any()) }
  }

  @Test
  fun `should return customers when name is informed`() {
    val name = UUID.randomUUID().toString()
    val fakeCustomers = listOf(buildCustomer(), buildCustomer())

    every { customerRepository.findByNameContaining(name) } returns fakeCustomers

    val customers = customerService.getAll(name)

    assertEquals(fakeCustomers, customers)
    verify(exactly = 0) { customerRepository.findAll() }
    verify(exactly = 1) { customerRepository.findByNameContaining(name) }
  }

  @Test
  fun `should create a customer and encrypt password`() {
    val fakePassword = UUID.randomUUID().toString()
    val initialPassword = Math.random().toString()
    val fakeCustomer = buildCustomer(password = initialPassword)
    val fakeCustomerEncrypted = fakeCustomer.copy(password = fakePassword)

    every { customerRepository.save(fakeCustomerEncrypted) } returns fakeCustomer
    every { bCrypt.encode(initialPassword) } returns fakePassword

    customerService.create(fakeCustomer)

    verify(exactly = 1) { customerRepository.save(fakeCustomerEncrypted) }
    verify(exactly = 1) { bCrypt.encode(initialPassword) }
  }


  @Test
  fun `should return a customer by id`() {
    val id = Random().nextInt()
    val fakeCustomer = buildCustomer(id = id)

    every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

    val customer = customerService.findById(id)

    assertEquals(fakeCustomer, customer)
    verify(exactly = 1) { customerRepository.findById(id) }
  }

  @Test
  fun `should throw an error when customer not found (findById)`() {
    val id = Random().nextInt()

    every { customerRepository.findById(id) } returns Optional.empty()

    val error = assertThrows<NotFoundException> { customerService.findById(id) }

    assertEquals("Customer [${id}], not exists.", error.message)
    assertEquals("ML-201", error.internalCode)
    verify(exactly = 1) { customerRepository.findById(id) }
  }

  @Test
  fun `should update a customer`() {
    val id = Random().nextInt()
    val fakeCustomer = buildCustomer(id = id)

    every { customerRepository.existsById(id) } returns true
    every { customerRepository.save(fakeCustomer) } returns fakeCustomer

    customerService.update(fakeCustomer)

    verify(exactly = 1) { customerRepository.existsById(id) }
    verify(exactly = 1) { customerRepository.save(fakeCustomer) }
  }

  @Test
  fun `should throw an error when customer not found (update)`() {
    val id = Random().nextInt()
    val fakeCustomer = buildCustomer(id = id)

    every { customerRepository.existsById(id) } returns false
    every { customerRepository.save(fakeCustomer) } returns fakeCustomer

    val error = assertThrows<NotFoundException> { customerService.update(fakeCustomer) }

    assertEquals("Customer [${id}], not exists.", error.message)
    assertEquals("ML-201", error.internalCode)

    verify(exactly = 1) { customerRepository.existsById(id) }
    verify(exactly = 0) { customerRepository.save(any()) }
  }

  @Test
  fun `should delete a customer`() {
    val id = Random().nextInt()
    val fakeCustomer = buildCustomer(id = id)
    val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.DESACTIVATE)

    every { customerService.findById(id) } returns fakeCustomer
    every { customerRepository.save(expectedCustomer) } returns expectedCustomer
    every { bookService.deleteByCustomer(fakeCustomer) } just runs

    customerService.delete(id)

    verify(exactly = 1) { customerService.findById(id) }
    verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
    verify(exactly = 1) { customerRepository.save(expectedCustomer) }
  }

  @Test
  fun `should throw an error when delete a customer that does not exists`() {
    val id = Random().nextInt()

    every { customerService.findById(id) } throws NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)

    val error = assertThrows<NotFoundException> { customerService.delete(id) }

    assertEquals("Customer [${id}], not exists.", error.message)
    assertEquals("ML-201", error.internalCode)

    verify(exactly = 1) { customerService.findById(id) }
    verify(exactly = 0) { bookService.deleteByCustomer(any()) }
    verify(exactly = 0) { customerRepository.save(any()) }
  }

  @Test
  fun `should return true when email is available`() {
    val email = "${Random().nextInt()}@email.com"

    every { customerRepository.existsByEmail(email) } returns false

    val emailAvailable = customerService.emailAvailable(email)

    assertTrue(emailAvailable)
    verify(exactly = 1) { customerRepository.existsByEmail(email) }
  }

  @Test
  fun `should return true when email is unavailable`() {
    val email = "${Random().nextInt()}@email.com"

    every { customerRepository.existsByEmail(email) } returns true

    val emailAvailable = customerService.emailAvailable(email)

    assertFalse(emailAvailable)
    verify(exactly = 1) { customerRepository.existsByEmail(email) }
  }
}