package xyz.lrhm.komakdast.ui.loadingScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.databinding.MainFragmentBinding
import xyz.lrhm.komakdast.ui.main.MainViewModel


/**
 */
class LoadingFragment : Fragment() {



    private val viewModel: MainViewModel by viewModels(
        ownerProducer = { requireActivity()}
    )
//    private lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

}