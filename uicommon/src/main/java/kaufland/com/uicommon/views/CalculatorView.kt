package kaufland.com.uicommon.views

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import android.widget.FrameLayout
import android.widget.TextView
import kaufland.com.uicommon.views.controller.CalculatorController
import kaufland.com.uicommon.views.keyboard.KeyboardController
import org.androidannotations.annotations.*
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.RoundingMode


@EViewGroup(resName = "calculator_view")
class CalculatorView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private var mCallback: Callback? = null

    @ViewById(resName = "appCompatImageButton")
    protected lateinit var mIcClose: MaterialButton

    @ViewById(resName = "quantity_edit")
    protected lateinit var mQuantityEdit: EditText

    @ViewById(resName = "textView")
    protected lateinit var mTitleTextView: TextView

    @ViewById(resName = "dynamic_content")
    protected lateinit var mDynamicContent: FrameLayout

    @Bean
    protected lateinit var mKeyboardController : KeyboardController

    interface Callback {
        fun onCloseClicked()
        fun onResult(result: Double)
    }


    @AfterViews
    protected fun init() {
        mIcClose.setOnClickListener { mCallback?.onCloseClicked() }
        mQuantityEdit?.setOnKeyListener { v, keyCode, event -> true }

    }

    fun bind(title: String, kolly: Int, callback: Callback, quantity: String?, piece: Boolean, dynamicView: View?) {

        mKeyboardController.bind(mQuantityEdit!!, kolly, piece, object :
            CalculatorController.OnResultListener {
            override fun onResult(result: Double) {
                callback.onResult(result)
            }
        }, false)
        mCallback = callback
        mTitleTextView.text = title

        mQuantityEdit?.setText(quantity ?: "")

        dynamicView?.let {
            mDynamicContent.removeAllViews()
            mDynamicContent.addView(dynamicView)
            dynamicView.layoutParams =
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dynamicView.requestLayout()
        }

    }

    fun isCalculated(): Boolean {
        return mQuantityEdit?.text.toString().toDoubleOrNull() != null
    }

    fun calculate(){
        mKeyboardController.mCalculatorController.ctrlEqualClicked()
    }


    @Click(resName = ["digit_1", "digit_0", "digit_2", "digit_3", "digit_4", "digit_5", "digit_6", "digit_7", "digit_8", "digit_9"])
    fun numberClicked(v: View?) {
       mKeyboardController.mCalculatorController.numberClicked((v as MaterialButton)?.text.toString().toInt())
    }

    @Click(resName = ["dec_point"])
    fun decPointClicked(v: View?) {
        mKeyboardController.mCalculatorController.decPointClicked()
    }

    @Click(resName = ["ctrl_del"])
    fun ctrlDeleteClicked(v: View?) {
        mKeyboardController.mCalculatorController.ctrlDeleteClicked()
    }

    @Click(resName = ["ctrl_equal"])
    fun ctrlEqualClicked(v: View?) {
        mKeyboardController.mCalculatorController.ctrlEqualClicked()
    }

    @Click(resName = ["digit_minus"])
    fun digitMinusClicked(v: View?) {
        mKeyboardController.mCalculatorController.digitMinusClicked()
    }

    @Click(resName = ["digit_plus"])
    fun digitPlusClicked(v: View?) {
        mKeyboardController.mCalculatorController.digitPlusClicked()
    }

    @Click(resName = ["digit_multiply"])
    fun digitMultiplyClicked(v: View?) {
        mKeyboardController.mCalculatorController.digitMultiplyClicked()
    }

    @Click(resName = ["digit_kolli"])
    fun digitKolliClicked(v: View?) {
        mKeyboardController.mCalculatorController.digitKolliClicked()
    }
}