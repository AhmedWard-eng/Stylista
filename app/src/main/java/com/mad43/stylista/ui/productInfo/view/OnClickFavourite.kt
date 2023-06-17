package com.mad43.stylista.ui.productInfo.view

import com.mad43.stylista.data.local.entity.Favourite

interface OnClickFavourite {
    fun onClick(product: Favourite)
}