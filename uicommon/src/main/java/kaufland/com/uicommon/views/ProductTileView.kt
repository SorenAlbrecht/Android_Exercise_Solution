package kaufland.com.uicommon.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

@EViewGroup(resName = "product_tile_view")
class ProductTileView(context: Context, attr: AttributeSet) : FrameLayout(context, attr) {


    @ViewById(resName = "product_title")
    protected lateinit var mProductTitleText: TextView

    @ViewById(resName = "product_klnr")
    protected lateinit var mProductKlnrText: TextView
    @ViewById(resName = "product_ean")
    protected lateinit var mProductEanText: TextView
    @ViewById(resName = "product_unit")
    protected lateinit var mProductUnitText: TextView
    @ViewById(resName = "product_price")
    protected lateinit var mProductPriceText: TextView

    fun bind(title: String?, klnr: String?, ean: String?, unit: String?, price: String?) {

        mProductTitleText.text = title
        mProductKlnrText.text = klnr
        mProductEanText.text = "GTIN $ean"
        mProductUnitText.text = unit
        mProductPriceText.text = price
    }

}
