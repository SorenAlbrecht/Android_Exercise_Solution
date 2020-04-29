package kaufland.com.uicommon.views.keyboard

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kaufland.com.uicommon.R
import kaufland.com.uicommon.views.controller.CalculatorController
import org.androidannotations.annotations.*
import org.mariuszgromada.math.mxparser.Expression

@EViewGroup(resName = "calculator_keyboard")
class CalculatorKeyboard(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    protected var mCalculatorController: CalculatorController? = null

    fun bind(editText: EditText, calculatorController: CalculatorController) {

        mCalculatorController = calculatorController
        editText?.setOnClickListener {
            visibility = View.VISIBLE
        }

    }

    @Click(resName = ["digit_1", "digit_0", "digit_2", "digit_3", "digit_4", "digit_5", "digit_6", "digit_7", "digit_8", "digit_9"])
    fun numberClicked(v: View?) {
        mCalculatorController?.numberClicked((v as MaterialButton)?.text.toString().toInt())
    }

    @Click(resName = ["dec_point"])
    fun decPointClicked(v: View?) {
        mCalculatorController?.decPointClicked()
    }

    @Click(resName = ["ctrl_del"])
    fun ctrlDeleteClicked(v: View?) {
        mCalculatorController?.ctrlDeleteClicked()
    }

    @Click(resName = ["ctrl_equal"])
    fun ctrlEqualClicked(v: View?) {
        mCalculatorController?.ctrlEqualClicked()
    }

    @Click(resName = ["digit_minus"])
    fun digitMinusClicked(v: View?) {
        mCalculatorController?.digitMinusClicked()
    }

    @Click(resName = ["digit_plus"])
    fun digitPlusClicked(v: View?) {
        mCalculatorController?.digitPlusClicked()
    }

    @Click(resName = ["digit_multiply"])
    fun digitMultiplyClicked(v: View?) {
        mCalculatorController?.digitMultiplyClicked()
    }

    @Click(resName = ["digit_kolli"])
    fun digitKolliClicked(v: View?) {
        mCalculatorController?.digitKolliClicked()
    }
}
