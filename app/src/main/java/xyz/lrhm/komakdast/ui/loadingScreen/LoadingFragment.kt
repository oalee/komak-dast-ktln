package xyz.lrhm.komakdast.ui.loadingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.databinding.FragmentLoadingBinding


/**
 */
@AndroidEntryPoint
class LoadingFragment : Fragment() {


    private val viewModel: LoadingViewModel by viewModels(
        ownerProducer = { this }
    )
    private lateinit var binding: FragmentLoadingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoadingBinding.inflate(inflater, container, false)


        viewModel.dataStatus.observe(viewLifecycleOwner) {

            when (it) {
                LoadingViewModel.DataStatus.Loaded -> {

                    findNavController().navigate(R.id.action_loadingFragment_to_mainFragment)
                }
                LoadingViewModel.DataStatus.Loading -> {
                }
                LoadingViewModel.DataStatus.Error -> {
                }
            }
        }

        return binding.root
    }

}