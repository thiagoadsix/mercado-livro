package com.mercadolivro.services

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helpers.buildPurchase
import com.mercadolivro.repositories.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {
  @MockK
  private lateinit var purchaseRepository: PurchaseRepository

  @MockK
  private lateinit var applicationEventPublisher: ApplicationEventPublisher

  @InjectMockKs
  private lateinit var purchaseService: PurchaseService

  private val purchaseEventSlot = slot<PurchaseEvent>()

  @Test
  fun `should create a purchase and publish event`() {
    val purchase = buildPurchase()

    every { purchaseRepository.save(purchase) } returns purchase
    every { applicationEventPublisher.publishEvent(any()) } just runs

    purchaseService.create(purchase)

    verify(exactly = 1) { purchaseRepository.save(purchase) }
    verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

    assertEquals(purchase, purchaseEventSlot.captured.purchaseModel)
  }

  @Test
  fun `should update a purchase`() {
    val purchase = buildPurchase()

    every { purchaseRepository.save(purchase) } returns purchase

    purchaseService.update(purchase)

    verify(exactly = 1) { purchaseRepository.save(purchase) }
  }
}