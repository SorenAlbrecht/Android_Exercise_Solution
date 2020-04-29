package kaufland.com.uicommon.views

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import androidx.fragment.app.Fragment
import org.androidannotations.annotations.SystemService
import kotlin.math.round

fun Fragment.isReadyForInvocation(): Boolean{
    return this != null && this.isAdded && this.isVisible && !this.isDetached
}

fun Activity.isReadyForInvocation(): Boolean{
    return this != null && !this.isFinishing && !this.isDestroyed
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

class Display {
    companion object {}
}

fun Display.Companion.windowHeight(windowManager: WindowManager): Int {

    var display = windowManager.defaultDisplay
    var size = Point()
    display.getSize(size)
    return size.y
}

val Any.TAG: String
    get() {
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23) name else name.substring(0, 23)// first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23) name else name.substring(name.length - 23, name.length)// last 23 chars
        }
    }

