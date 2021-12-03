package com.mercadolivro.exceptions

class NotFoundException(override val message: String, val internalCode: String): Exception() {
}