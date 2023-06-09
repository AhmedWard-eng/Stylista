package com.mad43.stylista.ui.registration.viewModel

sealed class SignUpState {
    class onSuccess(val message: Int) : SignUpState()
    class onError(val message: Int) : SignUpState()

    object BeforeValidation : SignUpState()

}