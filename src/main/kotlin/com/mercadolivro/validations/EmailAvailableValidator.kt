package com.mercadolivro.validations

import com.mercadolivro.services.CustomerService
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EmailAvailableValidator(private var customerService: CustomerService): ConstraintValidator<EmailAvailable, String> {
  override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
    if (value.isNullOrEmpty()) {
      return false
    }
    return customerService.emailAvailable(value)
  }
}