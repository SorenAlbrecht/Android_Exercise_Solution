package kaufland.com.hydraapi


import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import org.androidannotations.annotations.EBean
import java.util.*
import android.content.pm.PackageManager
import android.os.DeadObjectException
import kotlin.collections.ArrayList


@EBean(scope = EBean.Scope.Singleton)
class HydraApiClient : IHydraApi {

    companion object {
        @JvmField
        val TAG = HydraApiClient::class.java.simpleName

        val HYDRA_PACKAGE = "kaufland.com.hydra"

    }

    private var mHydraApi: IHydraApi? = null
    private var serviceConnection: RemoteServiceConnection? = null

    private var mApp: Application? = null

    private var mHydraServiceCallback: HydraServiceCallback? = null

    interface HydraServiceCallback {
        fun sessionExpired()
        fun onServiceConnected()
        fun onServiceDisconnected()
        fun onServiceDied()
    }

    fun isServiceConnected(): Boolean {
        return mHydraApi != null
    }

    @Throws(HydraNotInstalledException::class)
    fun connectService(app: Application, callback: HydraServiceCallback): Boolean {

        if (!isHydraInstalled(app)) {
            throw HydraNotInstalledException()
        }

        mApp = app
        mHydraServiceCallback = callback
        serviceConnection = RemoteServiceConnection()

        val i = createServiceIntent()
        return app.bindService(i, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun disconnectService() {
        try {
            mApp?.unbindService(serviceConnection)
        } catch (e: IllegalArgumentException) {
            //not registered
        }
    }

    private fun isHydraInstalled(app: Application): Boolean {

        var found = true

        try {

            app.packageManager.getPackageInfo(HYDRA_PACKAGE, 0)
        } catch (e: PackageManager.NameNotFoundException) {

            found = false
        }

        return found
    }

    private fun createServiceIntent(): Intent {
        val i = Intent("service.hydra")
        i.setPackage("kaufland.com.hydra")
        return i
    }

    override fun findById(id: String): MutableMap<String?, Any?>? {
        return exec { mHydraApi?.findById(id) as MutableMap<String?, Any?>? }
    }

    override fun findByType(type: String): List<Map<String, Any>>? {
        return exec { mHydraApi?.findByType(type) as List<Map<String, Any>>? } ?: ArrayList()
    }

    fun <T> exec(f: () -> T): T? {
        try {
            return f()
        } catch (e: SecurityException) {
            mHydraServiceCallback?.sessionExpired()
        } catch (e: DeadObjectException) {
            mHydraApi = null
            mHydraServiceCallback?.onServiceDied()
        } catch (e: RemoteException) {
            Log.e(TAG, "remote exception", e)
        }
        return null
    }

    override fun upsert(doc: Map<*, *>) {
        exec { mHydraApi?.upsert(doc) }
    }

    override fun delete(id: String) {
        exec { mHydraApi?.delete(id) }
    }

    override fun searchByFtsIndexAndValue(
        indexName: String?,
        value: String?,
        limit: Int
    ): MutableList<Map<String, *>>? {
        return exec { mHydraApi?.searchByFtsIndexAndValue(indexName, value, limit) as MutableList<Map<String, *>>? }
    }

    override fun syncState(id: String): String? {
        return exec { mHydraApi?.syncState(id) }
    }


    override fun makeReady(): Boolean {

        return exec {
            mHydraApi?.makeReady()
            true
        } ?: false
    }

    override fun asBinder(): IBinder {
        return mHydraApi!!.asBinder()
    }

    override fun logout() {
        exec { mHydraApi?.logout() }
    }

    override fun findByTypeAndItemType(type: String?, itemType: String?): List<Map<String, Any>>? {
        return exec { mHydraApi?.findByTypeAndItemType(type, itemType) as List<Map<String, Any>>? } ?: ArrayList()
    }


    internal inner class RemoteServiceConnection : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, boundService: IBinder) {
            mHydraApi = IHydraApi.Stub.asInterface(boundService)
            if (!makeReady()) {
                return
            }
            mHydraServiceCallback?.onServiceConnected()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mHydraApi = null
            mHydraServiceCallback?.onServiceDisconnected()
        }
    }
}
