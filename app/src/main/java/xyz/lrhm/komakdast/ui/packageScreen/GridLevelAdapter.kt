package xyz.lrhm.komakdast.ui.packageScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.util.SizeManager
import xyz.lrhm.komakdast.databinding.ItemLevelBinding
import javax.inject.Inject

@AndroidEntryPoint
class GridLevelAdapter(val parentFragment: Fragment, val levels: List<Lesson>) :
    RecyclerView.Adapter<GridLevelAdapter.ViewHolder>() {

    @Inject
    lateinit var sizeManager: SizeManager

    class ViewHolder(val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val size = (sizeManager.deviceWidth * 0.235).toInt()

        val binding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.root.layoutParams.width = size
        binding.root.layoutParams.height = size

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = levels[position]


//        val levelPosition: Int = page * 16 + position
//        if (levelPosition == 0 || levels[levelPosition].isResolved() || levels[levelPosition - 1].isResolved()) {
//            viewHolder.textView.setText(
//                Tools.numeralStringToPersianDigits(
//                    levels[levelPosition].getId().toString() + ""
//                )
//            )
//            viewHolder.textView.setVisibility(View.VISIBLE)
//            Glide.with(context).load(R.drawable.unlock).into(viewHolder.frame)
//            viewHolder.imageView.setVisibility(View.VISIBLE)
//        } else {
//            viewHolder.imageView.setVisibility(View.GONE)
//            viewHolder.textView.setVisibility(View.GONE)
//            Glide.with(context).load(R.drawable.level_locked).into(viewHolder.frame)
//        }

    }

    override fun getItemCount() = levels.size
}