package kaufland.com.uicommon.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import kaufland.com.uicommon.R
import org.androidannotations.annotations.EView
import org.androidannotations.annotations.SystemService

@EView
class TimeLineView(context: Context, attributeSet: AttributeSet?) : GridView(context, attributeSet) {

    @SystemService
    protected lateinit var mLayoutInflater: LayoutInflater

    fun bind(columns: Int, values: List<String>) {

        numColumns = columns

        adapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

                var view = (if(position%numColumns == 0) mLayoutInflater.inflate(R.layout.timeline_header_view, null) else mLayoutInflater.inflate(R.layout.timeline_cell_view, null)) as TextView
                view.text = values[position]
                return view
            }

            override fun getItem(position: Int): Any {
               return values[position]
            }

            override fun getItemId(position: Int): Long {
               return 0
            }

            override fun getCount(): Int {
                return values.size
            }

        }
    }
}