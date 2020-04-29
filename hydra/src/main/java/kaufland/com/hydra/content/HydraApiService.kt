package kaufland.com.hydra.content

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.couchbase.lite.CouchbaseLiteException
import com.couchbase.lite.MutableDocument
import kaufland.com.hydra.sync.CblPersistenceManager
import kaufland.com.hydraapi.IHydraApi
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EService
import java.util.*
import com.couchbase.lite.Database
import kaufland.com.hydra.util.ZipUtils
import java.io.File


@EService
class HydraApiService : Service() {

    @Bean
    protected lateinit var mPersistenceManager: CblPersistenceManager

    private val mBinder = object : IHydraApi.Stub() {
        override fun logout() {
            //nope
        }


        override fun makeReady(): Boolean {
            if (!Database.exists(CblPersistenceManager.DB_NAME, applicationContext.getFilesDir())) {
                ZipUtils.unzip(
                    application.assets.open("db.zip"),
                    applicationContext.filesDir
                )
                val path = File(applicationContext.filesDir, "db")
                try {
                    mPersistenceManager.copyDatabase(path)
                } catch (e: CouchbaseLiteException) {
                  Log.e(TAG, "failed", e)
                }

            }
            return true
        }


        @Throws(RemoteException::class)
        override fun findById(id: String): Map<*, *>? {

            makeReady()
            return mPersistenceManager!!.findDocumentById(id)
        }

        @Throws(RemoteException::class)
        override fun findByType(type: String): List<*> {

            makeReady()
            try {
                return mPersistenceManager!!.findDocumentsByType(type)
            } catch (e: CouchbaseLiteException) {
                Log.e(TAG, "failed findByType", e)
            }

            return ArrayList<Object>()
        }

        override fun findByTypeAndItemType(type: String?, itemType: String?): List<*> {
            makeReady()
            try {
                return mPersistenceManager!!.findDocumentsByTypeAndItemType(type, itemType)
            } catch (e: CouchbaseLiteException) {
                Log.e(TAG, "failed findByType", e)
            }

            return ArrayList<Object>()
        }

        @Throws(RemoteException::class)
        override fun upsert(upsert: Map<*, *>) {
            makeReady()
            var id = (upsert as MutableMap<String, Object>).remove("_id") as String
            val unsavedDoc = MutableDocument(id, upsert as Map<String, Object>)

            try {
                mPersistenceManager!!.upsert(unsavedDoc)
            } catch (e: CouchbaseLiteException) {
                Log.e(TAG, "failed upsert", e)
            }

        }

        override fun searchByFtsIndexAndValue(
            indexName: String?,
            value: String?,
            limit: Int
        ): MutableList<MutableMap<String, Any>>? {
            makeReady()
            try {
                var result =
                    mPersistenceManager!!.fullTextSearchForIndexAndType(
                        "GtinFtsIndex",
                        value,
                        limit
                    )
                return result
            } catch (e: CouchbaseLiteException) {
                Log.e(TAG, "failed delete", e)
            }
            return null
        }

        @Throws(RemoteException::class)
        override fun delete(id: String) {
            makeReady()
            try {
                mPersistenceManager!!.removeDocumentById(id)
            } catch (e: CouchbaseLiteException) {
                Log.e(TAG, "failed delete", e)
            }

        }

        @Throws(RemoteException::class)
        override fun syncState(id: String): String {
            return "bambam"
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()

        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    companion object {

        private val TAG = HydraApiService::class.java.name
    }

}
