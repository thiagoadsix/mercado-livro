package com.mercadolivro.exceptions

class AuthenticationException(override val message: String, val internalCode: String) : Exception() {
}