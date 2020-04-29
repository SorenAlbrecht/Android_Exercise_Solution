package kaufland.com.uicommon.views

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import kaufland.com.uicommon.R
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

@EViewGroup(resName = "image_text_button")
class ImageTextButton : MaterialCardView {

    @ViewById(resName = "icon")
    protected lateinit var mIconView: ImageView

    @ViewById(resName = "text")
    protected lateinit var mTextView: TextView

    private var mIcon: Int? = null

    private var mText: String? = null


    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initAttributes(attributeSet)
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton_) ?: return

        mIcon = typedArray.getResourceId(R.styleable.ImageTextButton__vIcon, R.drawable.ic_plus_button)
        mText = typedArray.getString(R.styleable.ImageTextButton__vText)
        typedArray.recycle()
    }

    @AfterViews
    protected fun init() {
        mIcon?.let { mIconView.setImageResource(it) };
        mText?.let { mTextView.text = mText }
    }

}
