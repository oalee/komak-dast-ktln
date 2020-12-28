package xyz.lrhm.komakdast.ui.packageScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.source.AppRepository
import xyz.lrhm.komakdast.core.util.SizeManager
import xyz.lrhm.komakdast.core.util.numeralStringToPersianDigits
import xyz.lrhm.komakdast.databinding.ItemLevelBinding

class GridLevelAdapter(
    val parentFragment: Fragment,
    val levels: List<Lesson>,
    val sizeManager: SizeManager,
    val appRepository: AppRepository
) :
    RecyclerView.Adapter<GridLevelAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val size = (sizeManager.deviceWidth * 0.235).toInt()

        val binding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val leftMargin = (sizeManager.deviceWidth - size * 4) / 5

        val lp = binding.root.layoutParams as RecyclerView.LayoutParams
        lp.width = size
        lp.height = size
        lp.topMargin = leftMargin
        lp.leftMargin = leftMargin

        binding.root.layoutParams = lp
//        binding.root.setBackgroundResource(R.color.colorAccent)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = levels[position]


//        val levelPosition: Int = page * 16 + position
        if (appRepository.isLessonOpen(item)) {
            holder.binding.itemLevelTextView.setText(
                item.id.toString().numeralStringToPersianDigits()
            )
            holder.binding.itemLevelTextView.visibility = View.VISIBLE
            Glide.with(parentFragment).load(R.drawable.unlock).into(holder.binding.itemLevelFrame)
        } else {
            holder.binding.itemLevelTextView.visibility = View.GONE
            Glide.with(parentFragment).load(R.drawable.level_locked)
                .into(holder.binding.itemLevelFrame)
        }

    }

    override fun getItemCount() = levels.size
}