package kaufland.com.uicommon.views.toolbar

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import kaufland.com.uicommon.views.TAG
import kaufland.com.uicommon.views.isReadyForInvocation
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import org.androidannotations.annotations.res.DimensionPixelOffsetRes
import java.lang.ref.WeakReference

@EBean(scope = EBean.Scope.Singleton)
class ToolbarHandler{

    private val TOOLBAR_NO_ELEVATION = 0

    @RootContext
    protected lateinit var mContext: Context

//    @DimensionPixelOffsetRes(R.dimen.tablayout_elevation)
    protected var mToolbarDefaultElevation: Int = 0

    private var mToolbar: Toolbar? = null

    private var mCurrentFragmentRef: WeakReference<Fragment>? = null

    fun init(toolbar: Toolbar?) {
        if (toolbar != null) {
            mToolbar = toolbar
        }
    }

    @Synchronized
    fun modifyToolbar(fragment: Fragment) {

        val toolbar = mToolbar
        toolbar?.post {

            try {
                mCurrentFragmentRef = WeakReference(fragment)
//                applyAppBarElevation(fragment, toolbar)

                if (fragment is TColor) {
                    toolbar.setBackgroundColor(castTo(TColor::class.java, fragment)!!.getToolbarColor())
                }

                if (fragment is TText) {
                    toolbar.title = (castTo(TText::class.java, fragment)!!.getToolbarText())
                }



//                if (fragment is TNavigationIcon) {
//                    val mTNavigationIcon = castTo(TNavigationIcon::class.java, fragment)
//
//                    toolbar.updateBackButton(mTNavigationIcon!!.getBackButton()) { v ->
//
//                        if (mTNavigationIcon!!.getBackButtonClickListener() != null) {
//                            mTNavigationIcon!!.getBackButtonClickListener().onClick()
//                        } else {
//                            if (mViewManager!!.getBackStackCount() === 1 && mViewManager!!.getActiveActivity() != null) {
//                                mViewManager!!.getActiveActivity().onBackPressed()
//                            } else {
//                                mViewManager!!.back()
//                            }
//                        }
//                    }
//                } else {
//                    toolbar.updateBackButton(null, null)
//                }

//                if (fragment is TRightIcons) {
//                    toolbar.getRightIconsContainer().setVisibility(View.VISIBLE)
//                    val rightIcons = castTo(TRightIcons::class.java, fragment)
//                    if (toolbar.getRightIconsContainer() != null) {
//                        toolbar.getRightIconsContainer().removeAllViews()
//
//                        for (i in 0 until rightIcons!!.getRightIconCount()) {
//
//                            var iconView = rightIcons!!.getCustomRightIconView(i)
//                            val rightIconView = getIconView(iconView)
//
//                            if (rightIconView == null) {
//                                return@toolbar.post
//                            }
//
//                            iconView = rightIconView
//
//                            if (iconView.getParent() != null) {
//                                (iconView.getParent() as ViewGroup).removeView(iconView)
//                            }
//
//                            toolbar.getRightIconsContainer().addView(iconView)
//
//                            if (i > 0) {
//                                val layoutParams = iconView.getLayoutParams() as LinearLayout.LayoutParams
//                                layoutParams.leftMargin =
//                                    mContext!!.resources.getDimensionPixelSize(R.dimen.toolbar_icon_margin)
//                                iconView.setLayoutParams(layoutParams)
//                            }
//                            rightIconView!!.setOnClickListener(rightIcons!!.getRightIconViewOnClickListener(i))
//                            if (LifecycleUtil.isReadyForInvocation(fragment)) {
//                                rightIcons!!.rightIconsAddedCallback(i)
//                            }
//                        }
//                    }
//                } else {
//                    if (toolbar.getRightIconsContainer() != null) {
//                        toolbar.getRightIconsContainer().removeAllViews()
//                    }
//                    toolbar.getRightIconsContainer().setVisibility(View.GONE)
//                }


            } catch (e: ToolbarNotReadyForInvocationException) {
                Log.e(TAG, "CustomToolbar not ready to be invoked. Fragment not longer active")
            } catch (e: Exception) {
                Log.e(TAG, "CustomToolbar not ready to be invoked.", e)
            }
        }
    }

//    private fun getIconView(iconView: View?): RightIconView? {
//        return if (iconView == null) RightIconView_.build(iconView!!.context) else if (iconView is RightIconView) iconView as RightIconView? else null
//    }

    @Throws(ToolbarNotReadyForInvocationException::class)
    private fun <T> castTo(type: Class<T>, source: Fragment): T? {

        if (source.isReadyForInvocation()) {
            return type.cast(source)
        }
        throw ToolbarNotReadyForInvocationException()
    }


    fun reloadToolbar() {
        if (mCurrentFragmentRef != null && mCurrentFragmentRef!!.get() != null) {
            modifyToolbar(mCurrentFragmentRef!!.get()!!)
        }
    }

    fun getToolbar(): Toolbar? {
        return if (mToolbar == null) {
            null
        } else mToolbar
    }
}
