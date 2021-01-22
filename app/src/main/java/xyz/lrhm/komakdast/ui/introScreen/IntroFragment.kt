package xyz.lrhm.komakdast.ui.introScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.core.util.SizeManager
import xyz.lrhm.komakdast.databinding.FragmentIntroBinding
import javax.inject.Inject


@AndroidEntryPoint
class IntroFragment : Fragment() {

    lateinit var binding: FragmentIntroBinding

    @Inject
    lateinit var sizeManager: SizeManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroBinding.inflate(inflater, container, false)

//        binding.container.layoutParams.height = sizeManager.deviceHeight

        Glide.with(this).load(R.drawable.package_0)
            .into(binding.imageView6)

        Glide.with(this).load(R.drawable.package_1)
            .into(binding.imageView5)

        Glide.with(this).load(R.drawable.package_2)
            .into(binding.imageView7)

        Glide.with(this).load(R.drawable.ok_button).into(binding.button)

        binding.button.setOnClickListener {

            findNavController().navigate(R.id.action_introFragment_to_mainFragment)

        }


        return binding.root
    }
}