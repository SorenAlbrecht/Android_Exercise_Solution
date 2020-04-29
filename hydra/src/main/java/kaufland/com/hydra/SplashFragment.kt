package kaufland.com.hydra

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kaufland.com.hydraapi.HydraApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment

@EFragment(R.layout.fragment_splash)
class SplashFragment : Fragment() {

    @Bean
    lateinit var mHydraApiClient: HydraApiClient

    @AfterViews
    protected fun init(){
        GlobalScope.launch {
            //TODO Fixme
            while (!mHydraApiClient.isServiceConnected()) {
            }
            findNavController().navigate(R.id.statusFragment)
        }
    }

}