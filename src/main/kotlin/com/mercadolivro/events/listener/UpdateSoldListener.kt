package com.mercadolivro.events.listener

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.services.BookService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class UpdateSoldListener(
  private val bookService: BookService
) {
  @Async
  @EventListener
  fun listen(purchaseEvent: PurchaseEvent) {
    bookService.purchase(purchaseEvent.purchaseModel.books)
  }
}