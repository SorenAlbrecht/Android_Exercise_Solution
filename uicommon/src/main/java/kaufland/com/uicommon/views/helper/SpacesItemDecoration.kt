package kaufland.com.uicommon.views.helper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val mSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace
        }

        outRect.left = mSpace
        outRect.right = mSpace
        outRect.bottom = mSpace
    }
}
