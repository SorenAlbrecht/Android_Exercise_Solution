package kaufland.com.uicommon.views.keyboard

import android.app.Activity
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import kaufland.com.uicommon.views.controller.CalculatorController
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import java.lang.ref.WeakReference

@EBean(scope = EBean.Scope.Singleton)
class KeyboardController {

    private var mCurrentActivityRef: WeakReference<Activity>? = null

    private var mKeyboardViewRef: WeakReference<CalculatorKeyboard>? = null

    @Bean
    lateinit var mCalculatorController: CalculatorController

    fun init(activity: Activity) {
        mCurrentActivityRef = WeakReference(activity)
        if (activity is KeyboardActivity) {
            mKeyboardViewRef = WeakReference(activity.keyboardView())
        }
    }

    fun show() {
        mKeyboardViewRef?.get()?.visibility = View.VISIBLE
    }

    fun hide() {
        mKeyboardViewRef?.get()?.visibility = View.GONE
    }

    fun bind(
        edit: EditText,
        kolly: Int,
        piece: Boolean,
        onResultListener: CalculatorController.OnResultListener?,
        calculatorKeyboardMode: Boolean
    ) {
        mCalculatorController.init(edit, piece, kolly, onResultListener)
        if (calculatorKeyboardMode) {
            mKeyboardViewRef?.get()?.bind(edit, mCalculatorController)
        }
    }

    fun triggerEqual(){
        mCalculatorController?.ctrlEqualClicked()
    }

    fun onKeyUp(keyCode: Int): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_0 -> mCalculatorController.numberClicked(0)
            KeyEvent.KEYCODE_1 -> mCalculatorController.numberClicked(1)
            KeyEvent.KEYCODE_2 -> mCalculatorController.numberClicked(2)
            KeyEvent.KEYCODE_3 -> mCalculatorController.numberClicked(3)
            KeyEvent.KEYCODE_4 -> mCalculatorController.numberClicked(4)
            KeyEvent.KEYCODE_5 -> mCalculatorController.numberClicked(5)
            KeyEvent.KEYCODE_6 -> mCalculatorController.numberClicked(6)
            KeyEvent.KEYCODE_7 -> mCalculatorController.numberClicked(7)
            KeyEvent.KEYCODE_8 -> mCalculatorController.numberClicked(8)
            KeyEvent.KEYCODE_9 -> mCalculatorController.numberClicked(9)
            KeyEvent.KEYCODE_NUMPAD_DOT -> mCalculatorController.decPointClicked()
            KeyEvent.KEYCODE_MINUS -> mCalculatorController.digitMinusClicked()
            KeyEvent.KEYCODE_NUMPAD_ADD,
            KeyEvent.KEYCODE_PLUS -> mCalculatorController.digitPlusClicked()
            KeyEvent.KEYCODE_NUMPAD_MULTIPLY -> mCalculatorController.digitMultiplyClicked()
            KeyEvent.KEYCODE_ENTER -> mCalculatorController.ctrlEqualClicked()
            KeyEvent.KEYCODE_DEL -> mCalculatorController.ctrlDeleteClicked()
            else -> return false
        }
        return true
    }


}