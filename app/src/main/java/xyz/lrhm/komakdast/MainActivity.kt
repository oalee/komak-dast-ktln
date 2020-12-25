package xyz.lrhm.komakdast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import xyz.lrhm.komakdast.ui.main.MainFragment
import xyz.lrhm.komakdast.ui.main.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewModel.config.observe(this){

        }

        lifecycleScope.launch {
            viewModel.appRepository.getAllLessons()

        }

    }
}