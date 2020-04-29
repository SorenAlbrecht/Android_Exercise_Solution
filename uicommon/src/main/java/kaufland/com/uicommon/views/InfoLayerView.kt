package kaufland.com.uicommon.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import kaufland.com.uicommon.R
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

@EViewGroup(resName = "info_layer_view")
class InfoLayerView(context: Context, attr: AttributeSet) : BadgeIconLayout(context, attr){

    @ViewById(resName = "text")
    protected lateinit var mText : TextView

    @AfterViews
    protected fun init(){
        setGravity(CIRCLE_BADGE_GRAVITY_RIGHT_CENTER)
        setBackgroundColor(resources.getColor(R.color.warning))
        bind(null)
    }

    fun bind(messages: List<String>?){
        if(messages == null || messages.isEmpty()){
            visibility = View.GONE
            return
        }
        visibility = View.VISIBLE

        when(messages?.size){
            1 -> {
                mText.text = messages[0]
                setBadgeVisibility(View.GONE)
            }
            else ->{
                mText.text = resources.getString(R.string.multible_alerts)
                badgeCount = messages.size.toString()
                setBadgeVisibility(View.VISIBLE)
            }
        }
    }

}
