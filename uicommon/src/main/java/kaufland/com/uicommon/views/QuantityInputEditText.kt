package kaufland.com.uicommon.views

import android.content.Context
import android.text.InputFilter
import android.text.method.KeyListener
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kaufland.com.uicommon.R
import org.androidannotations.annotations.*
import androidx.constraintlayout.solver.widgets.WidgetContainer.getBounds
import android.view.MotionEvent
import kaufland.com.uicommon.BuildConfig
import java.lang.NumberFormatException
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.KeyEvent
import android.view.View
import org.mariuszgromada.math.mxparser.Expression

@EView
class QuantityInputEditText(context: Context, attr: AttributeSet) :
    TextInputEditText(ContextThemeWrapper(context, R.style.QuantityEdit), attr, 0) {

    private var mClickListener: OnClickListener? = null

    @AfterViews
    protected fun init() {
        configureCalculatorDrawable()
    }

    private fun configureCalculatorDrawable() {


        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            if (mClickListener != null) context.getDrawable(R.drawable.ic_calc_icon) else null,
            null
        )
        setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            mClickListener?.let {
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= right - compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {

                        mClickListener?.onClick(this@QuantityInputEditText)
                        return@OnTouchListener true
                    }
                }
            }

            false
        })
    }

    fun bind(clickListener: OnClickListener?, text: String?) {
        mClickListener = clickListener
        text.let {
            setText(it)
        }
        configureCalculatorDrawable()

    }

    fun getQuantity(): Double? {
        return try {
            text?.toString()?.toDouble() ?: 0.0
        } catch (e: NumberFormatException) {
            null
        }
    }

}
