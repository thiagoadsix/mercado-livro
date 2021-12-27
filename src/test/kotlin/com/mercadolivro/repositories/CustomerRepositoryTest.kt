package com.mercadolivro.repositories

import com.mercadolivro.helpers.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {
  @Autowired
  private lateinit var customerRepository: CustomerRepository

  @BeforeEach
  fun setUp() = customerRepository.deleteAll()

  @Test
  fun `should return name containing`() {
    val customerJane = customerRepository.save(buildCustomer(name = "Jane"))
    val customerJan = customerRepository.save(buildCustomer(name = "Jan"))
    customerRepository.save(buildCustomer(name = "Rapha"))

    val customers = customerRepository.findByNameContaining("Ja")

    assertEquals(listOf(customerJan, customerJane), customers)
  }

  @Test
  fun `should return true when email exists`() {
    val email = "email@test.com"
    customerRepository.save(buildCustomer(email = email))

    val exists = customerRepository.existsByEmail(email)

    assertTrue(exists)
  }

  @Test
  fun `should return false when email do not exists`() {
    val email = "nonexistingemail@test.com"

    val exists = customerRepository.existsByEmail(email)

    assertFalse(exists)
  }
}