package kaufland.com.uicommon.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kaufland.com.uicommon.R
import kotlinx.coroutines.processNextEventInCurrentThread
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

@EViewGroup(resName = "info_layer_view_square")
class InfoLayerViewSquare(context: Context, attr: AttributeSet) : MaterialCardView(context, attr) {

    @ViewById(resName = "icon")
    protected lateinit var mIcon: ImageView

    @ViewById(resName = "text")
    protected lateinit var mText: TextView

    @ViewById(resName = "action")
    protected lateinit var mActionBtn: MaterialButton

    @ViewById(resName = "close")
    protected lateinit var mClose: ImageView

    fun bind(actionText: String, message: String, icon: Int?, closeClickedListener: OnClickListener?) {
        mText.text = message
        mActionBtn.text = actionText
        if (icon != null) {
            mIcon.setImageResource(icon)
            mIcon.visibility = View.VISIBLE
        } else {
            mIcon.visibility = View.GONE
        }
        if (closeClickedListener != null) {
            mClose.setOnClickListener(closeClickedListener)
            mClose.visibility = View.VISIBLE
        } else {
            mClose.visibility = View.GONE
        }
    }

}
