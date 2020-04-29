package kaufland.com.hydra.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.card.MaterialCardView
import kaufland.com.hydra.R
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

@EViewGroup(R.layout.status_category_view)
class StatusCategoryItemView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    @ViewById(R.id.text)
    protected lateinit var mTextView: TextView

    @ViewById(R.id.icon)
    protected lateinit var mImageView: ImageView

    @ViewById(R.id.card)
    protected lateinit var mCardView: MaterialCardView

    fun bind(icon : Int, text : Int) {

        mTextView.text = resources.getString(text)
        mImageView.setImageResource(icon)
    }

}
