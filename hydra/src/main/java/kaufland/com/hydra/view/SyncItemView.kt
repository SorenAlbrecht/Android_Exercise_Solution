package kaufland.com.hydra.view

import android.content.Context
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.text.Html
import android.util.AttributeSet
import android.widget.TextView
import com.couchbase.lite.*
import com.couchbase.lite.Function
import com.google.android.material.card.MaterialCardView
import kaufland.com.hydra.R
import kaufland.com.hydra.sync.CblPersistenceManager
import kaufland.com.hydra.sync.KQueryCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import java.lang.StringBuilder

@EViewGroup(R.layout.sync_item_view)
class SyncItemView(context: Context, attributeSet: AttributeSet) : MaterialCardView(context, attributeSet) {

    @ViewById(R.id.document_text)
    protected lateinit var mDocumentTextValue: TextView

    @Bean
    protected lateinit var mCblPersistenceManager: CblPersistenceManager

    protected var mHandler : Handler? = null;

    fun start() {


        mHandler = Handler()
        mHandler?.post(object : Runnable {
            override fun run() {
                execute()
                mHandler?.postDelayed(this, 2000)
            }
        })
    }

    fun execute(){
        GlobalScope.launch {

            var result = mCblPersistenceManager.executeQuery(KQueryCreator {
                QueryBuilder
                    .select(
                        SelectResult.property("type"),
                        SelectResult.expression(Function.count(Expression.string("*")))
                    )
                    .from(DataSource.database(it))
                    .groupBy(Expression.property("type"))
            })
            withContext(Dispatchers.Main){
                changed(result)
            }
        }


    }

    fun stop() {
       mHandler = null
    }

    fun changed(results: ResultSet) {
        var builder = StringBuilder()

        for (result in results) {
            if (builder.isNotEmpty()) {
                builder.appendln()
            }
            builder.append(result.getString(0))
            builder.append(": ")
            builder.append(result.getInt(1))
            builder.appendln()
        }

        mDocumentTextValue.text = builder.toString()
    }

}
