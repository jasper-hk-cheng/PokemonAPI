package tw.com.poke.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnAttach
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import tw.com.poke.api.databinding.ActivityMainBinding
import tw.com.poke.api.databinding.FragmentViewPagerBinding

@AndroidEntryPoint
class MyActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        with(viewBinding) {
            vpListBody.adapter = MyViewPagerAdapter(supportFragmentManager)
            TabLayoutMediator(tlTabs, vpListBody) { tab: TabLayout.Tab, position: Int ->
                tab.text = when (position) {
                    0 -> getString(R.string.tab_text_pokemon_list)
                    1 -> getString(R.string.tab_text_browser_record)
                    else -> ""
                }
            }.attach()
        }
    }
}

class MyViewPagerAdapter(
    private val fragmentManager: FragmentManager,
) : RecyclerView.Adapter<MyViewPagerAdapter.ViewHolder>() {

    override fun getItemCount(): Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = FragmentViewPagerBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // val originContainerId = holder.viewBinding.navHostFragment.id 不一定會用到
        holder.viewBinding.navHostFragment.run {
            id = View.generateViewId()
            doOnAttach { // onBindViewHolder時 view還沒attach到window 所以commitNow會找不到id
                val tag = "nav_host_fragment_$position"
                val fragment = fragmentManager.findFragmentByTag(tag)
                if (fragment == null) {
                    val navGraphResId = when (position) {
                        0 -> R.navigation.nav_graph_pokemon
                        1 -> R.navigation.nav_graph_resume
                        else -> 0
                    }
                    val navHostFragment = NavHostFragment.create(navGraphResId)
                    fragmentManager.beginTransaction()
                        .replace(holder.viewBinding.navHostFragment.id, navHostFragment, tag)
                        .commitNow()
                }
            }
        }
    }

    class ViewHolder(val viewBinding: FragmentViewPagerBinding) : RecyclerView.ViewHolder(viewBinding.root)
}
