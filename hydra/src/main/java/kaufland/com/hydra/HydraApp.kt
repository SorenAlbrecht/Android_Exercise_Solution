package kaufland.com.hydra

import android.app.Application
import android.widget.Toast
import kaufland.com.hydraapi.HydraApiClient
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EApplication

@EApplication
class HydraApp : Application(), HydraApiClient.HydraServiceCallback {

    @Bean
    protected lateinit var mHydraApiClient: HydraApiClient


    @AfterInject
    protected fun init() {
        mHydraApiClient.connectService(this, this)
    }

    override fun sessionExpired() {
    }

    override fun onServiceConnected() {
        Toast.makeText(this, "onServiceConnected()", Toast.LENGTH_SHORT).show()
    }

    override fun onServiceDisconnected() {
        Toast.makeText(this, "onServiceDisconnected()", Toast.LENGTH_SHORT).show()
    }

    override fun onServiceDied() {
        mHydraApiClient.connectService(this, this)
    }

}
