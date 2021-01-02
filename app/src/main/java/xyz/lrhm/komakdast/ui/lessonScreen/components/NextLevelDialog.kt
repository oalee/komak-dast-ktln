package xyz.lrhm.komakdast.ui.lessonScreen.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.util.legacy.LengthManager

class NextLevelDialog(
    context: Context,
    private val level: Lesson,
    val packageSize: Int,
    val skiped: Boolean,
    val prize: Int,
    val finishLevel: FinishLevel
) : Dialog(
    context
), View.OnClickListener {
    private val lengthManager: LengthManager = LengthManager(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_next_level)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val prizeTextView = findViewById<View>(R.id.prizeTextView) as TextView
        val scarf = findViewById<ImageView>(R.id.scarf_image_view)
        if (!level.resolved && !skiped) {
            val prizeString: String = if (prize == 30) "+۳۰" else if (prize == 10) "+۱۰" else "+۵"
            customizeTextView(prizeTextView, prizeString, lengthManager.getLevelAuthorTextSize())
            Glide.with(context).load(R.drawable.scarf).into(scarf)
        } else {
            prizeTextView.visibility = View.GONE
            scarf.visibility = View.GONE
        }
        val width = (lengthManager.screenWidth * 0.8f).toInt()
        val height = lengthManager.getHeightWithFixedWidth(
            R.drawable.dialog_background, (lengthManager.screenWidth * 0.8f).toInt()
        )
        val container: ConstraintLayout = findViewById(R.id.dialog_container)


//        container.setMaxWidth(width);
        container.minWidth = width
        container.minHeight = height
        val background = findViewById<ImageView>(R.id.dialog_background)
        val lp = background.layoutParams as ConstraintLayout.LayoutParams
        lp.width = width
        lp.height = height
        background.layoutParams = lp
        //        UiUtil.setWidth(background, width);
//        UiUtil.setHeight(background, height);
        Glide.with(getContext()).load(R.drawable.dialog_background).into(background)
        val nextButton = findViewById<ImageView>(R.id.next_level_button)
        Glide.with(getContext()).load(R.drawable.next_button_dialog).into(nextButton)
        nextButton.setOnClickListener(this)
        val homeButton = findViewById<ImageView>(R.id.home_button)
        Glide.with(getContext()).load(R.drawable.hoem_button).into(homeButton)
        homeButton.setOnClickListener(this)
        val tick = findViewById<ImageView>(R.id.tick)
        Glide.with(getContext()).load(R.drawable.tick).into(tick)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.home_button -> finishLevel.Home()
            R.id.next_level_button -> finishLevel.NextLevel()
        }
        dismiss()
    }

    private fun customizeTextView(textView: TextView, label: String, textSize: Float) {
        textView.text = label
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, lengthManager.getStoreItemFontSize())
        textView.setTextColor(Color.WHITE)
        textView.setShadowLayer(1f, 2f, 2f, Color.BLACK)
        //        textView.setTypeface(FontsHolder.getSansBold(textView.getContext()));
    }

}