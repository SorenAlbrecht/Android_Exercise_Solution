package kaufland.com.uicommon.views.controller

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.view.View
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import kaufland.com.uicommon.views.CalculatorView
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.SystemService
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.RoundingMode

@EBean
class CalculatorController {

    private var mOnResultListener: OnResultListener? = null
    private var mEditText: EditText? = null
    private var mPiece: Boolean = false
    private var mKolli: Int = 1

    @SystemService
    protected lateinit var mVibrator: Vibrator

    interface OnResultListener {
        fun onResult(result: Double)
    }

    fun init(editText: EditText, piece: Boolean, kolli: Int, onResultListener: OnResultListener?) {
        mPiece = piece
        mEditText = editText
        mKolli = kolli
        mOnResultListener = onResultListener
    }


    fun numberClicked(num: Int) {
        mVibrator.vibrate(100)
        mEditText?.let {
            var text = mEditText?.text!!
            if (isLastInputKolli(text)) {
                return
            }
            mEditText?.text?.append(num.toString())
        }
    }

    private fun isLastInputKolli(text: Editable): Boolean {
        return text.isNotEmpty() && text.length > 2 && KOLLI == text.substring(text.length - 2, text.length)
    }

    private fun isLastInputOperator(text: Editable): Boolean {
        if (text.isNotEmpty() && text.length > 1) {
            var possibleOperator = text.substring(text.length - 1, text.length)
            return possibleOperator == MINUS || possibleOperator == MULTIPLY || possibleOperator == PLUS
        }
        return false
    }

    fun decPointClicked() {
        mVibrator.vibrate(100)

        mEditText?.let {
            if (mPiece) {
                return
            }

            var text = mEditText?.text!!
            if (text.isNotEmpty() && text[text.length - 1].isDigit()) {
                text?.append(".")
            }
        }
    }


    fun ctrlDeleteClicked() {
        mVibrator.vibrate(100)

        mEditText?.let {
            var text = mEditText?.text!!
            if (text.isEmpty()) {
                return
            }

            if (isLastInputKolli(text)) {
                text.delete(text.length - 2, text.length)
                return
            }
            text.delete(text.length - 1, text.length)
        }
    }


    fun ctrlEqualClicked() {
        mVibrator.vibrate(100)
        mEditText?.let {
            var text = mEditText?.text!!
            calculate(map(text.toString()))
        }
    }

    private fun map(calc: String): String {
        var result = calc.replace(KOLLI, MULTIPLY + mKolli)
        return result.replace(MULTIPLY, "*")
    }

    private fun calculate(calc: String) {

        var ex = Expression(calc)
        var result = ex.calculate()
        if (!result.isNaN() && result >= 0) {
            mEditText?.setText(
                if (mPiece) result.toInt().toString() else BigDecimal(result).setScale(
                    3,
                    RoundingMode.HALF_EVEN
                ).toString()
            )
            mOnResultListener?.onResult(result)
        }

    }


    fun digitMinusClicked() {
        mVibrator.vibrate(100)

        mEditText?.let {
            var text = mEditText?.text!!
            if (text.isNotEmpty() && (text[text.length - 1].isDigit() || text.substring(text.length - 2) == KOLLI)) {
                text?.append(MINUS)
            }
        }
    }


    fun digitPlusClicked() {
        mVibrator.vibrate(100)
        mEditText?.let {
            var text = mEditText?.text!!
            if (text.isNotEmpty() && (text[text.length - 1].isDigit() || text.substring(text.length - 2) == KOLLI)) {
                text?.append(PLUS)
            }
        }
    }


    fun digitMultiplyClicked() {
        mVibrator.vibrate(100)
        mEditText?.let {
            var text = mEditText?.text!!
            if (text.isNotEmpty() && (text[text.length - 1].isDigit() || text.substring(text.length - 2) == KOLLI)) {
                text?.append(MULTIPLY)
            }
        }
    }


    fun digitKolliClicked() {
        mVibrator.vibrate(100)
        mEditText?.let {
            var text = mEditText?.text!!
            if (text.isNotEmpty() && text[text.length - 1].isDigit()) {
                text?.append(KOLLI)
            }
            if (text.isNotEmpty() && isLastInputOperator(text)) {
                if (text.length > 3 && text.substring(text.length - 3, text.length - 1) == KOLLI) {
                    return
                }

                text.delete(text.length - 1, text.length)
                text?.append(KOLLI)
            }
        }
    }


    companion object {
        var KOLLI = "KI"
        var MULTIPLY = "x"
        var PLUS = "+"
        var MINUS = "-"
    }
}
