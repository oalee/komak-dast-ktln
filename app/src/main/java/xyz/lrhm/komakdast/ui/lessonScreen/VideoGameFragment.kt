package xyz.lrhm.komakdast.ui.lessonScreen

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.source.AppRepository
import xyz.lrhm.komakdast.core.util.SizeManager
import xyz.lrhm.komakdast.core.util.legacy.ImageManager
import xyz.lrhm.komakdast.core.util.legacy.ImageManager.Companion.getInstance
import xyz.lrhm.komakdast.core.util.legacy.LengthManager
import xyz.lrhm.komakdast.core.util.legacy.Tools
import xyz.lrhm.komakdast.ui.lessonScreen.VideoGameFragmentDirections.Companion.actionVideoGameFragmentSelf
import xyz.lrhm.komakdast.ui.lessonScreen.components.CheatDrawable
import xyz.lrhm.komakdast.ui.lessonScreen.components.FinishLevel
import xyz.lrhm.komakdast.ui.lessonScreen.components.KeyboardView
import xyz.lrhm.komakdast.ui.lessonScreen.components.KeyboardView.OnKeyboardEvent
import xyz.lrhm.komakdast.ui.lessonScreen.components.NextLevelDialog
import javax.inject.Inject

@AndroidEntryPoint
class VideoGameFragment : Fragment(), OnKeyboardEvent, View.OnClickListener {
    private var levelId: Int? = null
    private var packageId: Int? = null
    var keyboardView: KeyboardView? = null
    var keyboardContainer: FrameLayout? = null
    var playerView: PlayerView? = null
    var player: SimpleExoPlayer? = null
    var lengthManager: LengthManager? = null
    var imageManager: ImageManager? = null
    var tools: Tools? = null
    var imageView: ImageView? = null
    var level: Lesson? = null
    var cheatButton: ImageView? = null
    private var packageSize = 0
    private lateinit var cheatButtons: Array<View>
    private var skiped = false
    private var resulved = false
    private var blackWidow: View? = null
    private var lastLevelId = 0
    var root: View? = null

    val args: VideoGameFragmentArgs by navArgs()

    @JvmField
    @Inject
    var appRepository: AppRepository? = null

    @JvmField
    @Inject
    var sizeManager: SizeManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video_game, container, false)
        root = view

        level = appRepository!!.getLesson(args.levelKey)
        levelId = level!!.id
        packageId = level!!.packageId

        //        lengthManager = ((MainApplication) getActivity().getApplication()).getLengthManager();
//        imageManager = ((MainApplication) getActivity().getApplication()).getImageManager();
//        db = DBAdapter.getInstance(getActivity());
//        coinAdapter = new CoinAdapter(getActivity(), getActivity());
        lengthManager = LengthManager(requireContext())
        imageManager = getInstance(requireContext())
        tools = Tools(requireContext())

//        level = appRepository.getLesson(levelId);
        lastLevelId = appRepository!!.getLastLevelIdForPackage(packageId!!)
        packageSize = appRepository!!.getPackageSize(packageId!!)
        cheatButton = view.findViewById(R.id.cheat_button)
        cheatButton!!.setOnClickListener(View.OnClickListener { toggleCheatButton() })
        playerView = view.findViewById(R.id.player_view)
        setUpImagePlace(view)
        initExoPlayer()
        imageView = view.findViewById<View>(R.id.imageView) as ImageView

//        imageView.setOnClickListener(this);

//        imagePath = "file://" + getActivity().getFilesDir().getPath() + "/Packages/package_" + packageId + "/" + level.getResources();
        if (level!!.type === Lesson.Type.OnePic) {
            Glide.with(this).load(level!!.getImagesPath()!![0]).into(imageView!!)
            imageView!!.setOnClickListener { nextLevel(5) }
        }
        if (level!!.type === Lesson.Type.FourPics) {
            imageView!!.visibility = View.GONE
            init4Pics(view)
            view.findViewById<View>(R.id.four_pic_next_button).setOnClickListener { }
        }
        if (level!!.type === Lesson.Type.Keyboard) {
            imageView!!.visibility = View.GONE
            keyboardContainer =
                view.findViewById<View>(R.id.fragment_game_keyboard_container) as FrameLayout
            keyboardContainer!!.visibility = View.VISIBLE
            keyboardView = KeyboardView(context, level!!.answer!!, sizeManager!!)
            keyboardView!!.onKeyboardEvent = this
            keyboardContainer!!.addView(keyboardView)
            Glide.with(this).load(R.drawable.cheat_button).into(cheatButton!!)
        }
        return view
    }

    var areCheatsVisible = false
    fun toggleCheatButton() {
        disableCheatButton(false)
        if (!areCheatsVisible) {
            Glide.with(this).load(R.drawable.next_button).into(cheatButton!!)
            areCheatsVisible = true
            showCheats()
        } else {
            Glide.with(this).load(R.drawable.cheat_button).into(cheatButton!!)
            areCheatsVisible = false
            hideCheats()
        }
    }

    fun disableCheatButton(enable: Boolean) {
        cheatButton!!.isClickable = enable
    }

    fun init4Pics(parent: View) {
        val imageView1 = parent.findViewById<ImageView>(R.id.imageView1)
        val imageView2 = parent.findViewById<ImageView>(R.id.imageView2)
        val imageView3 = parent.findViewById<ImageView>(R.id.imageView3)
        val imageView4 = parent.findViewById<ImageView>(R.id.imageView4)
        val fourPics = arrayOf(imageView1, imageView2, imageView3, imageView4)
        val pics = level!!.getImagesPath()
        for (i in 0..3) {
            fourPics[i].visibility = View.VISIBLE
            fourPics[i].setTag(R.integer.click_id_four, i)
            fourPics[i].setOnClickListener { view ->
                //                    ((ImageView)view).setImageTintList(ColorStateList.valueOf(Color.parseColor("A5FF0000")));
                var highlightColor = Color.parseColor("#A5FF0000")
                val value = view.getTag(R.integer.click_id_four) as Int
                if (level!!.getImagesPath()!![value].contains(level!!.answer!!)) {
                    highlightColor = Color.parseColor("#A500FF00")
                    //                        parent.findViewById(R.id.four_pic_next_button).setVisibility(View.VISIBLE);
                    nextLevel(10)
                }
                (view as ImageView).setColorFilter(highlightColor)


//
//
//                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
//
//                    Paint redHighLight = new Paint();
//                    redHighLight.setColorFilter(colorFilter);
//                    redHighLight.setAlpha(190);
//
//                    ((ImageView) view).setColorFilter(redHighLight.getColorFilter());
            }
            //            fourPics[i].setTag(level.getPics());
            Glide.with(this).load(pics!![i]).into(fourPics[i])
        }
    }

    fun initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(context)
        playerView!!.player = player
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "nashenavayan")
        )
        val mediaUri = Uri.parse(level!!.getVideoPath())
        val mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri)
        player!!.prepare(mediaSource)
        player!!.setPlayWhenReady(true)
        player!!.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL)
        playerView!!.hideController()
        //        playerView.setUseController(false);

//        playerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                player.setPlayWhenReady(!isPlaying());
//
//                return false;
//            }
//        });
    }

    private val isPlaying: Boolean
        private get() = player != null && player!!.playbackState != Player.STATE_ENDED && player!!.playbackState != Player.STATE_IDLE && player!!.playWhenReady

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) player!!.release()
    }

    private fun setUpImagePlace(view: View) {
        val box = view.findViewById<View>(R.id.box) as FrameLayout
        tools!!.resizeView(
            box,
            lengthManager!!.getLevelImageWidth(),
            lengthManager!!.getLevelImageHeight()
        )
        val frame = view.findViewById<View>(R.id.frame) as ImageView
        tools!!.resizeView(
            frame,
            lengthManager!!.getLevelImageFrameWidth(),
            lengthManager!!.getLevelImageFrameHeight()
        )
        Glide.with(this).load(R.drawable.frame).into(frame)
        //        frame.setImageBitmap(imageManager.loadImageFromResource(R.drawable.frame, lengthManager.getLevelImageFrameWidth(), lengthManager.getLevelImageFrameHeight()));
        if (level!!.type === Lesson.Type.Keyboard) {
            cheatButtons = arrayOf(
                view.findViewById(R.id.cheat_remove_some_letters),
                view.findViewById(R.id.cheat_reveal_a_letter),
                view.findViewById(R.id.cheat_skip_level)
            )
            for (cheatView in cheatButtons) {
                cheatView.setOnClickListener(this)
                val layoutParams = cheatView.layoutParams
                layoutParams.width = lengthManager!!.getCheatButtonWidth()
                layoutParams.height = lengthManager!!.getCheatButtonHeight()
            }
            val cheatTitles = arrayOf(
                "حذف چند حرف",
                "نمایش یک حرف",
                "رد کردن مرحله"
            )
            val cheatCosts = intArrayOf(
                0,
                0,
                0
            )
            for (i in 0..2) tools!!.setViewBackground(
                cheatButtons[i],
                CheatDrawable( // TODO: 8/7/15 bad performance
                    view.context,
                    i,
                    cheatTitles[i],
                    ""
                )
            )
            blackWidow = view.findViewById(R.id.black_widow)
        }
    }

    private fun nextLevel(prize: Int) {
        appRepository!!.resolveLevel(level!!)
        //        db.resolveLevel(packageId, levelId);
        resulved = true

//        tools.backUpDB();
        NextLevelDialog(
            requireContext(), level!!, packageSize, skiped, prize,
            object : FinishLevel {
                override fun NextLevel() {
                    if (level!!.id == lastLevelId) {
                        Navigation.findNavController(root!!).navigateUp()
                        return
                    }
                    val next = appRepository!!.getNextLevel(level!!)
                    val directions = actionVideoGameFragmentSelf(
                        next!!.key!!
                    )
                    Navigation.findNavController(root!!).navigate(directions)
                }

                override fun Home() {
                    Navigation.findNavController(root!!).navigateUp()
                }
            }).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onAllAnswered(guess: String?) {
        if (guess!!.replace("آ", "ا").replace("/", "") == level!!.answer!!.replace(
                ".",
                ""
            ).replace("آ", "ا").replace("/", "")
        ) {


//            if (!level.isResolved()) {
//                coinAdapter.earnCoins(CoinAdapter.LEVEL_COMPELETED_PRIZE);
//            }
            if (!skiped) nextLevel(30)
        }
    }

    override fun onHintClicked() {}
    private var lastTimeClicked: Long = 0
    private val treshHold: Long = 850
    override fun onClick(view: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastTimeClicked < treshHold) return
        lastTimeClicked = clickTime
        when (view.id) {
            R.id.cheat_remove_some_letters -> {
                toggleCheatButton()
                cheatHazf()
            }
            R.id.cheat_reveal_a_letter -> {
                toggleCheatButton()
                cheatAzafe()
            }
            R.id.cheat_skip_level -> {
                toggleCheatButton()
                cheatNext()
            }
        }
    }

    fun showCheats() {
        for (view in cheatButtons) view.visibility = View.VISIBLE
        blackWidow!!.visibility = View.VISIBLE
        val animatorSet = AnimatorSet()
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(blackWidow, "alpha", 0f, 0.60f),
            ObjectAnimator.ofFloat(
                cheatButtons[0],
                "translationX",
                -cheatButtons[0].width.toFloat(),
                0f
            ),
            ObjectAnimator.ofFloat(
                cheatButtons[1],
                "translationX",
                +cheatButtons[1].width.toFloat(),
                0f
            ),
            ObjectAnimator.ofFloat(
                cheatButtons[2],
                "translationX",
                -cheatButtons[2].width.toFloat(),
                0f
            )
        )
        animatorSet.setDuration(600).start()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                if (activity == null) return
                disableCheatButton(true)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    fun hideCheats() {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(blackWidow, "alpha", 0.60f, 0f),
            ObjectAnimator.ofFloat(
                cheatButtons[0],
                "translationX",
                0f,
                -cheatButtons[0].width.toFloat()
            ),
            ObjectAnimator.ofFloat(
                cheatButtons[1],
                "translationX",
                0f,
                +cheatButtons[1].width.toFloat()
            ),
            ObjectAnimator.ofFloat(
                cheatButtons[2],
                "translationX",
                0f,
                -cheatButtons[2].width.toFloat()
            )
        )
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                for (view in cheatButtons) {
                    view.visibility = View.GONE
                    view.clearAnimation()
                }
                blackWidow!!.visibility = View.GONE
                if (activity == null) return
                disableCheatButton(true)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorSet.setDuration(600).start()
    }

    private fun cheatNext() {
        if (level!!.resolved) {
            skiped = true
            while (keyboardView!!.setAnswered()) {
            }
            keyboardView!!.setAdditionalClickListener { nextLevel(30) }
            //            nextLevel(0);
        } else {
            skiped = true
            resulved = true
            appRepository!!.resolveLevel(level!!)
            while (keyboardView!!.setAnswered()) {
            }
            keyboardView!!.setAdditionalClickListener { nextLevel(30) }
            //            nextLevel(30);
        }
    }

    private fun cheatHazf() {
        if (level!!.resolved) {
            keyboardView!!.removeSome()
        } else {
            if (!keyboardView!!.removeSome()) {
                val toastText = "نمیشه دیگه"
                //                ToastMaker.show(getContext(), toastText, Toast.LENGTH_SHORT);
//                coinAdapter.earnCoins(CoinAdapter.ALPHABET_HIDING_COST);
            }
        }
    }

    private fun cheatAzafe() {
        if (level!!.resolved) {
            keyboardView!!.showOne()
        } else {
            if (!keyboardView!!.showOne()) {
                val toastText = "نمیشه دیگه"
            }
        }
    }
}