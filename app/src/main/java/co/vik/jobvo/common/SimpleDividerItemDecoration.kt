package co.vik.jobvo.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SimpleDividerItemDecoration(
    context: Context, @ColorInt color: Int,
    @FloatRange(from = 0.0, fromInclusive = false) heightDp: Float
) :
    ItemDecoration() {
    private val mPaint: Paint
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val params = view.layoutParams as RecyclerView.LayoutParams

        // we want to retrieve the position in the list
        val position = params.viewAdapterPosition

        // and add a separator to any view but the last one
        if (position < state.itemCount) {
            outRect[0, 0, 0] = mPaint.strokeWidth.toInt() // left, top, right, bottom
        } else {
            outRect.setEmpty() // 0, 0, 0, 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // we set the stroke width before, so as to correctly draw the line we have to offset by width / 2
        val offset = (mPaint.strokeWidth / 2).toInt()

        // this will iterate over every visible view
        for (i in 0 until parent.childCount) {
            // get the view
            val view = parent.getChildAt(i)
            val params = view.layoutParams as RecyclerView.LayoutParams

            // get the position
            val position = params.viewAdapterPosition

            // and finally draw the separator
            if (position < state.itemCount) {
                c.drawLine(
                    view.left.toFloat(),
                    (view.bottom + offset).toFloat(),
                    view.right.toFloat(),
                    (view.bottom + offset).toFloat(),
                    mPaint
                )
            }
        }
    }

    /**
     * Create a decoration that draws a line in the given color and width between the items in the view.
     *
     * @param context  a context to access the resources.
     * @param color    the color of the separator to draw.
     * @param heightDp the height of the separator in dp.
     */
    init {
        mPaint = Paint()
        mPaint.color = color
        val thickness = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightDp, context.resources.displayMetrics
        )
        mPaint.strokeWidth = thickness
    }
}
