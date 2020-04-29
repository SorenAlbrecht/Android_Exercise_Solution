package kaufland.com.uicommon.views.statusbar

import android.app.Activity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.androidannotations.annotations.EBean
import java.lang.ref.WeakReference

@EBean(scope = EBean.Scope.Singleton)
class StatusbarHandler {

    private var mCurrentActivityRef: WeakReference<Activity>? = null

    fun init(activity: Activity) {
        mCurrentActivityRef = WeakReference(activity)
    }

    fun modify(fragment: Fragment) {
        if(fragment is SBarColor){
            var activity = mCurrentActivityRef?.get()
            activity?.let {
                it.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                it.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                it.window.statusBarColor = fragment.getStatusBarColor()
            }
        }
    }


}