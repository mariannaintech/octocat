package com.octocat.ui.home.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalMarginsDecoration(
    private val margins: VerticalMargins,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val curPosition = parent.getChildAdapterPosition(view)
        val lastPosition = parent.adapter!!.itemCount - 1
        when (curPosition) {
            0 -> outRect.top = margins.top
            lastPosition -> outRect.bottom = margins.bottom
            else -> super.getItemOffsets(outRect, view, parent, state)
        }
    }

}

data class VerticalMargins(
    val top: Int,
    val bottom: Int
)
