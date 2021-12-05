package com.mercadolivro.events

import com.mercadolivro.services.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.*

@Component
class GenerateNfeListener(
  private val purchaseService: PurchaseService
) {
  @EventListener
  fun listen(purchaseEvent: PurchaseEvent) {
    val nfe = UUID.randomUUID().toString()
    val purchaseModel = purchaseEvent.purchaseModel.copy(nfe = nfe)
    println("To aqui no listener => $nfe, $purchaseModel")
    purchaseService.update(purchaseModel)
  }
}