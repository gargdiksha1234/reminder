package ru.ifr0z.notify.utils

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeToDeleteCallback(
    private val deleteIcon: Drawable,
    private val backgroundColor: ColorDrawable,
    private val onSwiped: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val intrinsicWidth = deleteIcon.intrinsicWidth
    private val intrinsicHeight = deleteIcon.intrinsicHeight
    private val padding = 10 // Additional padding on top

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onSwiped(position)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Draw background color
        backgroundColor.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        backgroundColor.draw(canvas)

        // Calculate position of delete icon
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2 + padding
        val iconBottom = iconTop + intrinsicHeight-15
        val iconRight = itemView.right - iconMargin
        val iconLeft = itemView.right - iconMargin - intrinsicWidth

        // Draw delete icon
        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        deleteIcon.draw(canvas)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 20 // Increase swipe escape velocity
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.20f // Increase swipe threshold
    }

    override fun getBoundingBoxMargin(): Int {
        return intrinsicHeight // Increase bounding box margin
    }
}
