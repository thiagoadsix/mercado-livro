package com.mercadolivro.services

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.models.PurchaseModel
import com.mercadolivro.repositories.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
  private val purchaseRepository: PurchaseRepository,
  private val applicationEventPublisher: ApplicationEventPublisher
) {
  fun create(purchaseModel: PurchaseModel) {
    purchaseRepository.save(purchaseModel)
    println("To aqui no PurchaseService (create) => $purchaseModel")
    applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
  }

  fun update(purchaseModel: PurchaseModel) {
    println("To aqui no PurchaseService (update) => $purchaseModel")
    purchaseRepository.save(purchaseModel)
  }
}
