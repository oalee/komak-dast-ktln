package xyz.lrhm.komakdast.ui.packageScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.databinding.FragmentPackageBinding
import xyz.lrhm.komakdast.ui.main.MainViewModel

@AndroidEntryPoint
class PackageFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    private lateinit var binding: FragmentPackageBinding

    val args: PackageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPackageBinding.inflate(inflater, container, false)

        val levels =
            viewModel.appRepository.cachedLessons!!.filter { it.packageId == args.packageId }
        var pageSize = levels.size / 16
        if (levels.size % 16 != 0) {
            pageSize++
        }

        Glide.with(this).load(R.drawable.top_seperator).into(binding.topBarImageView)
        Glide.with(this).load(R.drawable.bottom_seperator).into(binding.bottomBarImageView)

        binding.viewPager.adapter =
            ScreenSlidePagerAdapter(childFragmentManager, pageSize, args.packageId)

        binding.viewpagertab.setViewPager(binding.viewPager)
//        FragmentStatePagerAdapter()
//
//        val fragmentPagerItemsCreator: FragmentPagerItems.Creator = FragmentPagerItems.with(
//            activity
//        )
//        for (i in 0 until pageSize) {
//            fragmentPagerItemsCreator.add(
//                "", LevelsFragment::class.java,
//                Bundler().putInt(xyz.lrhm.komakdast.View.Fragment.PackageFragment.LEVEL_PAGE, i)
//                    .putInt("id", packageId).get()
//            )
//        }
//
//        val fragmentPagerItems: FragmentPagerItems = fragmentPagerItemsCreator.create()
//        val adapter = FragmentPagerItemAdapter(
//            childFragmentManager, fragmentPagerItems
//        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // TODO: Use the ViewModel
    }

    class ScreenSlidePagerAdapter(fm: FragmentManager, val pageSize: Int, val packageId: Int) :
        FragmentStatePagerAdapter(
            fm,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
        override fun getCount(): Int = pageSize

        override fun getItem(position: Int): Fragment {

            val args = GridLevelFragmentArgs(packageId, position)

            val fragment = GridLevelFragment()
            fragment.arguments = args.toBundle()

            return fragment

        }
    }

}
