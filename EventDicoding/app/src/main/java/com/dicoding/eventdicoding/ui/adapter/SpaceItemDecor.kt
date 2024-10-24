package com.dicoding.eventdicoding.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecor(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.top = space
        outRect.right = space
        outRect.bottom = space

        if(parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        }
    }
}