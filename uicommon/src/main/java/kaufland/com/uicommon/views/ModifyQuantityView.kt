package kaufland.com.uicommon.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.material.button.MaterialButton
import kaufland.com.uicommon.R
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

@EViewGroup(resName = "modify_quantity_view")
class ModifyQuantityView(context : Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet){

    private var mCallback: Callback? = null

    @Click(resName = ["add_kolli"])
    protected fun addKolli(){
        mCallback?.onAddKolli()
    }

    @Click(resName = ["remove_kolli"])
    protected fun removeKolli(){
        mCallback?.onRemoveKolli()
    }

    @Click(resName = ["add_piece"])
    protected fun addPiece(){
        mCallback?.onAddPiece()
    }

    @Click(resName = ["remove_piece"])
    protected fun removePiece(){
        mCallback?.onRemovePiece()
    }

    interface Callback{
        fun onAddPiece()
        fun onRemovePiece()
        fun onAddKolli()
        fun onRemoveKolli()
    }

    fun init(callback: Callback){
        mCallback = callback
    }

}
