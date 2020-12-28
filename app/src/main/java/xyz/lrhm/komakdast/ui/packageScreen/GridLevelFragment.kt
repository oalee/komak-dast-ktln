package xyz.lrhm.komakdast.ui.packageScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.databinding.FragmentGridLevelsBinding
import xyz.lrhm.komakdast.ui.main.MainViewModel

@AndroidEntryPoint
class GridLevelFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    private lateinit var binding: FragmentGridLevelsBinding

    val args: GridLevelFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGridLevelsBinding.inflate(inflater, container, false)

        val lessons =
            viewModel.appRepository.getCachedLessonsForPackageAndPage(args.packageId, args.pageId)

        val adapter = GridLevelAdapter(
            this, lessons, viewModel.sizeManager, viewModel.appRepository
        )

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // TODO: Use the ViewModel
    }

}