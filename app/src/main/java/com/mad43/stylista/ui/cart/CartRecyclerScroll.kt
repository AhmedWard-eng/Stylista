package com.mad43.stylista.ui.cart

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

private var MINIMUM = 25
abstract class CartRecyclerScroll : RecyclerView.OnScrollListener() {
    var scrollDist = 0
    var isVisible = true
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (isVisible && scrollDist > MINIMUM) {
            hide()
            scrollDist = 0
            isVisible = false
        }
        else if (!isVisible && scrollDist < -MINIMUM) {
            show()
            scrollDist = 0
            isVisible = true
        }
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy
        }
//        if (!isVisible && dy > 0){
//            show()
//            isVisible = true
//        }
    }



    abstract fun show()
    abstract fun hide()
}