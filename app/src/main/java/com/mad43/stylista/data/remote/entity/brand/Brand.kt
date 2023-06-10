package com.mad43.stylista.data.remote.entity.brand

import com.mad43.stylista.domain.model.DisplayBrand

data class Brand(
    val id: Long?,
    val handle: String?,
    val title: String?,
    val updated_at: String?,
    val body_html: String?,
    val published_at: String?,
    val sort_order: String?,
    val template_suffix: Any?,
    val disjunctive: Boolean?,
    val rules: List<Rules>?,
    val published_scope: String?,
    val admin_graphql_api_id: String?,
    val image: Image?

)

fun Brand.mapRemoteBrandToDisplayBrand(): DisplayBrand {
    return DisplayBrand(this.title ?: "", image?.src ?: "")
}
