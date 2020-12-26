package xyz.lrhm.komakdast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import xyz.lrhm.komakdast.databinding.MainActivityBinding
import xyz.lrhm.komakdast.ui.main.MainFragment
import xyz.lrhm.komakdast.ui.main.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

//        Glide.with(this).load(R.drawable.background).centerCrop().into(binding.backgroundImageView)

//        viewModel.config.observe(this){
//
//        }
//
//        lifecycleScope.launch {
//            viewModel.appRepository.getAllLessons()
//
//        }

//        viewModel.localUtil.parseLocalJson()

    }
}