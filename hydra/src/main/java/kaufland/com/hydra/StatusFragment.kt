package kaufland.com.hydra

import androidx.fragment.app.Fragment
import kaufland.com.hydra.view.DeviceItemView
import kaufland.com.hydra.view.StatusCategoryItemView
import kaufland.com.hydra.view.SyncItemView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById


@EFragment(R.layout.status_fragment)
class StatusFragment : Fragment() {

    @ViewById(R.id.device_header_view)
    protected lateinit var mDeviceHeaderView: StatusCategoryItemView

    @ViewById(R.id.device_item_view)
    protected lateinit var mDeviceItemView: DeviceItemView

    @ViewById(R.id.sync_header_view)
    protected lateinit var mSyncHeaderView: StatusCategoryItemView

    @ViewById(R.id.sync_item_view)
    protected lateinit var mSyncItemView: SyncItemView

    @AfterViews
    protected fun init(){
        mDeviceHeaderView.bind(R.drawable.ic_round_device, R.string.device)
        mSyncHeaderView.bind(R.drawable.ic_round_cloud, R.string.data)
        mDeviceItemView.bind()
    }

    override fun onResume() {
        super.onResume()
        mSyncItemView.start()
    }

    override fun onStop() {
        super.onStop()
        mSyncItemView.stop()
    }

}
