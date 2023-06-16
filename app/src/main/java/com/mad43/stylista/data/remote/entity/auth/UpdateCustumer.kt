package com.mad43.stylista.data.remote.entity.auth

import com.google.gson.annotations.SerializedName
import com.mad43.stylista.data.remote.entity.SignupModel

data class UpdateCustumer ( val customer: UpdateCustumerModel)
data class UpdateCustumerModel (val last_name: String, val note : String)