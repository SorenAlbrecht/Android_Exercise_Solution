package kaufland.com.hydra.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.card.MaterialCardView
import kaufland.com.hydra.BuildConfig
import kaufland.com.hydra.R
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import android.util.DisplayMetrics
import android.view.WindowManager
import org.androidannotations.annotations.SystemService


@EViewGroup(R.layout.device_item_view)
class DeviceItemView(context: Context, attributeSet: AttributeSet) : MaterialCardView(context, attributeSet) {

    @ViewById(R.id.secured_key_value)
    protected lateinit var mSecuredKeyValue: TextView

    @ViewById(R.id.device_value)
    protected lateinit var mDeviceValue: TextView

    @ViewById(R.id.screen_value)
    protected lateinit var mScreenValue: TextView

    @ViewById(R.id.screen_density_value)
    protected lateinit var mScreenDensityValue: TextView

    @ViewById(R.id.sdk_value)
    protected lateinit var mSdkValue: TextView

    @SystemService
    protected lateinit var mWindowManager: WindowManager

    fun bind() {

        val dm = DisplayMetrics()
        mWindowManager.defaultDisplay.getMetrics(dm)

        mSecuredKeyValue.text = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        mDeviceValue.text = Build.DEVICE
        mScreenValue.text = "${dm.widthPixels}px / ${dm.heightPixels}px"
        mScreenDensityValue.text = (dm.density * 160f).toInt().toString()
        mSdkValue.text = "${Build.VERSION.RELEASE} -> ${Build.VERSION.SDK_INT}"


    }

}
