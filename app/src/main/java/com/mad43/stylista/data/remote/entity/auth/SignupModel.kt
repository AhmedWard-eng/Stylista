package com.mad43.stylista.data.remote.entity

import com.google.gson.annotations.SerializedName


data class SignupRequest( val customer: SignupModel)
data class SignupModel (val email: String,
                        val first_name: String = "",
                        val last_name: String = "",
                        @SerializedName("tags") var password: String,
                        val note : String = "",
                        var multipass_identifier : String = "")