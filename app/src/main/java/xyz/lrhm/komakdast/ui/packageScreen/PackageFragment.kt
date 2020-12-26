package xyz.lrhm.komakdast.ui.packageScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.databinding.MainFragmentBinding
import xyz.lrhm.komakdast.ui.main.MainViewModel

@AndroidEntryPoint
class PackageFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MainFragmentBinding.inflate(inflater, container, false)

        Glide.with(this).load(R.drawable.package_0).into(binding.puzImageView)
        Glide.with(this).load(R.drawable.package_1).into(binding.alfImageView)
        Glide.with(this).load(R.drawable.package_2).into(binding.keyboardImageView)

        binding.textView.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_introFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // TODO: Use the ViewModel
    }

}