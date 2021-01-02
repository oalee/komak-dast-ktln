package xyz.lrhm.komakdast.ui.lessonScreen.components

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.RelativeLayout
import dagger.hilt.android.AndroidEntryPoint
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.core.util.SizeManager
import xyz.lrhm.komakdast.core.util.legacy.ImageManager
import xyz.lrhm.komakdast.core.util.legacy.LengthManager
import xyz.lrhm.komakdast.core.util.legacy.SizeConverter
import xyz.lrhm.komakdast.core.util.legacy.SizeConverter.Companion.SizeConvertorFormHeight
import xyz.lrhm.komakdast.core.util.legacy.SizeConverter.Companion.SizeConvertorFromWidth
import xyz.lrhm.komakdast.ui.lessonScreen.components.KeyboardView.KeyView
import java.util.*
import javax.inject.Inject

/**
 * for whom reads this code , i have wrote this code in my darkest hours
 * even i . the creator cannot understand this code . the only thing matters is this works
 * if you want to make a change , you'll have better luck writing a new one.
 * may the force be with you
 * use the source luke
 */
@AndroidEntryPoint
class KeyboardView(context: Context?, solution: String, sizeManager: SizeManager) :
    RelativeLayout(context) {
    @JvmField
    var onKeyboardEvent: OnKeyboardEvent? = null
    var answerConverter: SizeConverter
    var buttons: Array<KeyView?>
    var answers: Array<KeyView?>
    var leftX: Int
    var rightX: Int
    var heightSwipe: Int
    var allStrings = "ضثقفغعهخحشسیبلاتنمکگچجظطژزرذدپو"
    var swipeView: SwipeView
    var showedData = ""
    var levelAnswer: String
    @Inject
    lateinit var imageManager: ImageManager
    var buttonConvertor: SizeConverter
    var solution: String
    var mReaminingLenght: Double
    var mMargin: Int
    private var mListener: OnClickListener? = null

    @Inject
    lateinit var sizeManager: SizeManager
    fun getAnswersInALine(
        answer: String, context: Context?,
        isTwoLine: Boolean, lineNumber: Int, otherAnswerCount: Int
    ) {
        val length = answer.length
        var middle = sizeManager.deviceWidth / 2
        var offSet = answerConverter.width / 30
        if (length == 1) {
            offSet += answerConverter.width / 2
        }
        val temp = arrayOfNulls<KeyView>(answer.length)
        val answerCointainer = RelativeLayout(context)
        val answerParams = LayoutParams(layoutParams)
        var topMargin = 0
        if (isTwoLine) {
            if (lineNumber == 1) {
                topMargin = mMargin
            }
            if (lineNumber == 2) {
                topMargin = (mMargin + answerConverter.height * 1.1).toInt()
            }
        } else {
            topMargin = mMargin
        }
        val indexer = if (lineNumber == 1) 0 else otherAnswerCount
        answerParams.topMargin = topMargin
        val top = answerParams.topMargin
        var leftOffset = 0
        var rightOffset = 0
        for (i in 0 until length / 2) {
            middle = sizeManager.deviceWidth / 2
            offSet = 0 //answerConverter.mWidth / 50;
            if (length % 2 == 1) {
                offSet += if (answer[length / 2] == ' ') answerConverter.width / 4 else answerConverter.width / 2
            }
            val paramsRight = LayoutParams(
                layoutParams
            )
            val paramsLeft = LayoutParams(
                layoutParams
            )
            paramsLeft.leftMargin = middle - (i + 1) * answerConverter.width - offSet + leftOffset
            paramsRight.leftMargin = middle + offSet + (i
                    * answerConverter.width) + rightOffset
            var state = answer[length / 2 - (i + 1)] == ' '
            if (state) {
                rightOffset -= answerConverter.width / 2
                paramsRight.leftMargin += rightOffset
            }
            val rightKey = KeyView(
                context,
                if (i == length / 2 - 1) Companion.TYPE_RIGHT else Companion.TYPE_CENTER,
                Companion.MODE_ANSWER,
                state
            )
            state = answer[length / 2
                    + (i + if (length % 2 == 1) 1 else 0)] == ' '
            if (state) {
                leftOffset += answerConverter.width / 2
                paramsLeft.leftMargin += leftOffset
            }
            val leftKey = KeyView(
                context,
                if (i == length / 2 - 1) Companion.TYPE_LEFT else Companion.TYPE_CENTER,
                Companion.MODE_ANSWER,
                state
            )
            rightKey.setXY(paramsRight.leftMargin, top)
            leftKey.setXY(paramsLeft.leftMargin, top)
            rightKey.index = length / 2 - (i + 1) + indexer
            leftKey.index = (length / 2 + (i + if (length % 2 == 1) 1 else 0)
                    + indexer)
            temp[length / 2 + (i + if (length % 2 == 1) 1 else 0)] = leftKey
            temp[length / 2 - (i + 1)] = rightKey

            // answers[solution.length() / 2 + i + 1] = leftKey;
            //
            // answers[solution.length() / 2 + i + 1].index = solution
            // .length() / 2 + i + 1;
            //
            // answers[solution.length() / 2 - i - 1] = rightKey;
            // answers[solution.length() / 2 - i - 1].index = solution
            // .length() / 2 - i - 1;
            answerCointainer.addView(leftKey, paramsLeft)
            answerCointainer.addView(rightKey, paramsRight)
        }
        if (length % 2 == 1) {
            val mmiddle = sizeManager.deviceWidth / 2
            val moffSet = answerConverter.width / 2
            val paramsLamp = LayoutParams(
                layoutParams
            )
            val state = answer[length / 2] == ' '
            val center = KeyView(
                context, Companion.TYPE_CENTER,
                Companion.MODE_ANSWER, state
            )
            paramsLamp.leftMargin = mmiddle - moffSet
            center.setXY(paramsLamp.leftMargin, top)
            answerCointainer.addView(center, paramsLamp)
            temp[length / 2] = center
            temp[length / 2]!!.index = length / 2 + indexer
            // answers[solution.length() / 2] = center;
            // answers[solution.length() / 2].index = solution.length()
            // / 2;
        }
        for (i in temp.indices) {
            answers[i + indexer] = temp[i]!!
        }
        addView(answerCointainer, answerParams)
    }

    val emptyAnswersCount: Int
        get() {
            var res = 0
            for (answer in answers) if (answer!!.otherIndex == -1 && answer.state != Companion.STATE_EMPTY) res++
            return res
        }
    val buttonEmptyCount: Int
        get() {
            var res = 0
            for (answer in buttons) {
                if (answer!!.otherIndex == -1 && answer.state == Companion.STATE_NORMAL) res++
            }
            return res
        }
    val availableGuesses: String
        get() {
            var res = ""
            for (key in buttons) if (key!!.otherIndex == -1 && key.state != Companion.STATE_REMOVED && key.state != Companion.STATE_TOUCHED) {
                res += key.character.toString() + ""
            }
            return res
        }

    fun lenOfCharInString(string: String, character: Char?): Int {
        var len = 0
        for (i in 0 until string.length) {
            if (string[i] == character) len++
        }
        return len
    }

    fun removeSome(): Boolean {
        var len = buttonEmptyCount / 4
        if (len >= buttonEmptyCount
            - solution.replace("/", "").replace(" ", "").length
        ) return false
        while (len != 0) {
            while (true) {
                val random = Random().nextInt(buttons.size)
                if (buttons[random]!!.otherIndex == -1 && buttons[random]!!.state != Companion.STATE_REMOVED && buttons[random]!!.state != Companion.STATE_TOUCHED) {
                    val lenInAnswer = lenOfCharInString(
                        levelAnswer,
                        buttons[random]!!.character
                    )
                    val lenInButtons = lenOfCharInString(
                        availableGuesses,
                        buttons[random]!!.character
                    )
                    if (lenInButtons > lenInAnswer) {
                        buttons[random]!!.setRemoved()
                        len--
                    }
                    if (len == 0) break
                }
            }
        }
        return true
    }

    fun setAdditionalClickListener(listener: OnClickListener?) {
        mListener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            if (mListener != null) {
                mListener!!.onClick(this)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun findIndexOfCharacter(character: Char?): Int {
        if (!solution.contains("/")) {
            for (i in answers.indices) {
                if (answers[i]!!.otherIndex == -1
                    && answers[i]!!.state != Companion.STATE_EMPTY
                ) {
                    // solution.replace("/", "").){
                    // Logger.d("keyboard" , "find it" + i);
                    val ans = solution
                    if (ans[i] == character) return i
                    // return i;
                }
            }
        } else {
            val temp1 = solution.split("/").toTypedArray()
            for (i in answers.indices) {
                if (answers[i]!!.otherIndex == -1
                    && answers[i]!!.state != Companion.STATE_EMPTY
                ) {
                    // solution.replace("/", "").){
                    // Logger.d("keyboard" , "find it" + i);
                    var ans = temp1[0]
                    if (i < ans.length) if (ans[i] == character) return i
                    ans = temp1[1]
                    if (i >= temp1[0].length) if (ans[i - temp1[0].length] == character) return i
                    // return i;
                }
            }
        }
        return -1
    }

    fun showOne(): Boolean {
        val answerCount = emptyAnswersCount
        val buttonEmptyCount = buttonEmptyCount
        if (answerCount == 0 || buttonEmptyCount == 0) return false
        val temp = levelAnswer
        while (true) {
            val random = Random().nextInt(levelAnswer.length)
            val character = levelAnswer[random]
            temp.replace(character.toString() + "", "")
            for (key in buttons) {
                if (key!!.character == character && key.state == Companion.STATE_NORMAL) {
                    if (findIndexOfCharacter(key.character) != -1) {
                        var temp2 = ""
                        for (i in 0 until levelAnswer.length) if (i != random) temp2 += levelAnswer[i].toString() + ""
                        levelAnswer = temp2
                        key.setAnswered()
                        return true
                    }
                }
            }
            if (temp.length == 0) return false
        }

        // return true;
    }

    fun setAnswered(): Boolean {
        val answerCount = emptyAnswersCount
        val buttonEmptyCount = buttonEmptyCount
        if (answerCount == 0 || buttonEmptyCount == 0) return false
        clearAnswers()
        val temp = levelAnswer
        while (true) {
            val random = Random().nextInt(levelAnswer.length)
            val character = levelAnswer[random]
            temp.replace(character.toString() + "", "")
            for (key in buttons) {
                if (key!!.character == character && key.state == Companion.STATE_NORMAL) {
                    if (findIndexOfCharacter(key.character) != -1) {
                        var temp2 = ""
                        for (i in 0 until levelAnswer.length) if (i != random) temp2 += levelAnswer[i].toString() + ""
                        levelAnswer = temp2
                        key.setAnswered()
                        return true
                    }
                }
            }
            if (temp.length == 0) return false
        }

        // return true;
    }

    fun getUnTocuhDrawableForButton(type: Int): Int {
        when (type) {
            Companion.TYPE_CENTER -> return R.drawable.keyboardcenterbutton
            Companion.TYPE_LEFT -> return R.drawable.button_left
            Companion.TYPE_RIGHT -> return R.drawable.button_right
        }
        return 0
    }

    fun clearAnswers() {
        for (i in answers.indices) {
            val thisView = answers[i]
            if (thisView!!.otherIndex != -1
                && thisView.state != Companion.STATE_ANSWERED
            ) {
                thisView.state = Companion.STATE_NORMAL
                val index = thisView.otherIndex
                thisView.otherIndex = -1
                buttons[index]!!.state = Companion.STATE_NORMAL
                buttons[index]!!.otherIndex = -1
                buttons[index]!!.imgView
                    ?.setImageBitmap(
                        imageManager
                            ?.loadImageFromResource(
                                getUnTocuhDrawableForButton(buttons[index]!!.type),
                                buttonConvertor.height,
                                buttonConvertor.width,
                                ImageManager.ScalingLogic.FIT
                            )
                    )
                val fromX = +thisView.x - buttons[index]!!.x
                val fromY = +thisView.y - buttons[index]!!.y
                val toX = 0
                val toY = 0
                val translateAnimation = TranslateAnimation(
                    fromX.toFloat(), toX.toFloat(), fromY.toFloat(), toY.toFloat()
                )
                translateAnimation.fillAfter = true
                translateAnimation.duration = 400
                val scaleX = (answerConverter.width.toFloat()
                        / buttonConvertor.width)
                val scaleY = (answerConverter.height.toFloat()
                        / buttonConvertor.height)
                val scaleAnimation = ScaleAnimation(
                    scaleX, 1f,
                    scaleY, 1f
                )
                scaleAnimation.fillAfter = true
                scaleAnimation.duration = 400
                val set = AnimationSet(true)
                set.fillAfter = true
                set.addAnimation(scaleAnimation)
                set.addAnimation(translateAnimation)
                buttons[index]!!.charTemp!!.startAnimation(set)
            }
        }
    }

    interface OnKeyboardEvent {
        fun onAllAnswered(guess: String?)
        fun onHintClicked()
    }

    val isAllAnswered: Boolean
        get() {
            for (answer in answers) if (answer!!.otherIndex == -1 && answer.state != Companion.STATE_EMPTY) return false
            return true
        }
    val firstEmptyIndexAnswers: Int
        get() {
            for (i in answers.indices) if (answers[i]!!.otherIndex == -1
                && answers[i]!!.state != Companion.STATE_EMPTY
            ) return i
            return -1
        }

    // int len = temp.length();
    // String res = "";
    // for (int i = len - 1; i >= 0; i--)
    // res += temp.toCharArray()[i];
    val allAswers: String?
        get() {
            var temp: String? = ""
            for (answer in answers) {
                temp += if (answer!!.otherIndex != -1) buttons[answer.otherIndex]!!.character else " "
            }
            // int len = temp.length();
            // String res = "";
            // for (int i = len - 1; i >= 0; i--)
            // res += temp.toCharArray()[i];
            return temp
        }

    inner class SwipeView(context: Context?) : View(context) {
        var xStart = 0f
        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                xStart = event.x
            }
            if (event.action == MotionEvent.ACTION_MOVE) {
                if (xStart <= leftX && event.x >= rightX
                    || xStart >= rightX && event.x <= leftX
                ) if (event.y <= heightSwipe && event.y >= 0) {
                    clearAnswers()
                }
            }
            return true
        }
    }

    inner class KeyView : RelativeLayout {
        var x = -1
        var y = -1
        var otherIndex = -1
        var index = 0
        var imgView: ImageView? = null
        var charHolder: ImageView? = null
        var charTemp: ImageView? = null
        var isCharTempAdded = false
        var character: Char? = null
        var state = -1
        var type = 0
        var mode = 0
        fun setRemoved() {
            state = Companion.STATE_REMOVED
            val animation = AlphaAnimation(1f, 0f)
            animation.fillAfter = true
            animation.duration = 1000
            val animation2 = AlphaAnimation(1f, 0f)
            animation2.fillAfter = true
            animation2.duration = 1000
            charHolder!!.startAnimation(animation2)
            charTemp!!.startAnimation(animation)
            val drawable = getTocuhedDrawableForButton(type)
            imgView!!.setImageBitmap(
                imageManager!!.loadImageFromResource(
                    drawable,
                    buttonConvertor.width, buttonConvertor.height,
                    ImageManager.ScalingLogic.FIT
                )
            )
        }

        fun setAnswered() {
            state = Companion.STATE_REMOVED
            val mIndex = findIndexOfCharacter(character)
            answers[mIndex]!!.otherIndex = index
            answers[mIndex]!!.state = Companion.STATE_ANSWERED
            otherIndex = mIndex
            var answerDrawable = 0
            when (answers[mIndex]!!.type) {
                Companion.TYPE_CENTER -> answerDrawable = R.drawable.answer_vasat_green
                Companion.TYPE_LEFT -> answerDrawable = R.drawable.answer_left_green
                Companion.TYPE_RIGHT -> answerDrawable = R.drawable.answer_right_green
            }
            answers[mIndex]!!.imgView!!.setImageBitmap(
                imageManager
                    ?.loadImageFromResource(
                        answerDrawable,
                        answerConverter.width, answerConverter.height,
                        ImageManager.ScalingLogic.FIT
                    )
            )
            val drawable = getTocuhedDrawableForButton(type)
            imgView!!.setImageBitmap(
                imageManager!!.loadImageFromResource(
                    drawable,
                    buttonConvertor.width, buttonConvertor.height, ImageManager.ScalingLogic.FIT
                )
            )
            charHolder!!.visibility = GONE
            val toX = answers[mIndex]!!.x - x
            val toY = answers[mIndex]!!.y - y
            val translateAnimation = TranslateAnimation(
                0f,
                toX.toFloat(), 0f, toY.toFloat()
            )
            translateAnimation.fillAfter = true
            translateAnimation.duration = 400
            val scaleX = (answerConverter.width.toFloat()
                    / buttonConvertor.width)
            val scaleY = (answerConverter.height.toFloat()
                    / buttonConvertor.height)
            val scaleAnimation = ScaleAnimation(
                1f, scaleX, 1f,
                scaleY
            )
            scaleAnimation.fillAfter = true
            scaleAnimation.duration = 400
            if (!isCharTempAdded) {
                val params = LayoutParams(
                    buttonConvertor.width,
                    buttonConvertor.height
                )
                params.leftMargin = x
                params.topMargin = y
                this@KeyboardView.addView(charTemp, params)
                isCharTempAdded = true
            }
            val set = AnimationSet(true)
            set.fillAfter = true
            set.addAnimation(scaleAnimation)
            set.addAnimation(translateAnimation)
            charTemp!!.startAnimation(set)
            if (isAllAnswered) if (onKeyboardEvent != null) onKeyboardEvent!!.onAllAnswered(
                allAswers
            )
        }

        // LAMP
        @JvmOverloads
        constructor(
            context: Context?,
            type: Int = 0,
            mode: Int = 3,
            character: Char? = '-'
        ) : super(context) {
            mInit(context, type, mode, character)
        }

        constructor(context: Context?, type: Int, mode: Int, state: Boolean) : super(context) {
            this.state = if (state) Companion.STATE_EMPTY else Companion.STATE_NORMAL
            if (state) {
                layoutParams = LayoutParams(
                    answerConverter.width / 2, answerConverter.height
                )
                return
            } else mInit(context, type, mode, character)
        }

        var yaroConvertor: SizeConverter? = null
        fun mInit(
            context: Context?, type: Int, mode: Int,
            character: Char?
        ) {
            this.mode = mode
            this.type = type
            if (state == -1) state = Companion.STATE_NORMAL
            yaroConvertor = SizeConvertorFormHeight(
                sizeManager.deviceHeight * 0.75, 1200, 1447, sizeManager
            )


//            Logger.d(TAG, "answerconverter width is " + answerConverter.getWidth());
            layoutParams = if (mode == Companion.MODE_ANSWER) {
                LayoutParams(
                    answerConverter.width, answerConverter.height
                )
            } else {
                LayoutParams(
                    buttonConvertor.width, buttonConvertor.height
                )
            }
            imgView = ImageView(context)
            imgView!!.layoutParams = layoutParams
            imgView!!.scaleType = ScaleType.FIT_XY
            charHolder = ImageView(context)
            charHolder!!.layoutParams = layoutParams
            charHolder!!.scaleType = ScaleType.FIT_XY
            charTemp = ImageView(context)
            charTemp!!.layoutParams = layoutParams
            charTemp!!.scaleType = ScaleType.FIT_XY
            setText(character)
            addView(imgView)
            addView(charHolder)
            var drawAble = 0
            var height = 0
            var width = 0
            when (type) {
                Companion.TYPE_LEFT -> when (mode) {
                    Companion.MODE_ANSWER -> {
                        drawAble = R.drawable.answer_left
                        height = answerConverter.height
                        width = answerConverter.width
                    }
                    Companion.MODE_BUTTON -> {
                        drawAble = R.drawable.button_left
                        height = buttonConvertor.height
                        width = buttonConvertor.width
                    }
                    else -> {
                    }
                }
                Companion.TYPE_RIGHT -> when (mode) {
                    Companion.MODE_ANSWER -> {
                        drawAble = R.drawable.answer_right
                        height = answerConverter.height
                        width = answerConverter.width
                    }
                    Companion.MODE_BUTTON -> {
                        drawAble = R.drawable.button_right
                        height = buttonConvertor.height
                        width = buttonConvertor.width
                    }
                    else -> {
                    }
                }
                Companion.TYPE_CENTER -> {
                    if (mode == Companion.MODE_ANSWER) {
                        drawAble = R.drawable.answer_vasat
                        height = answerConverter.height
                        width = answerConverter.width
                    }
                    if (mode == Companion.MODE_BUTTON) {
                        drawAble = R.drawable.keyboardcenterbutton
                        height = buttonConvertor.height
                        width = buttonConvertor.width
                    }
                }
                else -> {
                }
            }
            if (mode == Companion.MODE_LAMP) {
                drawAble = R.drawable.hint
                height = buttonConvertor.height
                width = buttonConvertor.width
            }
            imgView!!.setImageBitmap(
                imageManager!!.loadImageFromResource(
                    drawAble, height, width,
                    ImageManager.ScalingLogic.FIT
                )
            )
            if (mode == Companion.MODE_LAMP) {
                drawAble = R.drawable.hint
                height = buttonConvertor.height
                width = buttonConvertor.width
                imgView!!.setOnClickListener { if (onKeyboardEvent != null) onKeyboardEvent!!.onHintClicked() }
            } else if (mode == Companion.MODE_ANSWER) {
                imgView!!.setOnClickListener(OnClickListener {
                    val thisView = this@KeyView
                    if (thisView.otherIndex == -1
                        || thisView.state == Companion.STATE_ANSWERED
                    ) return@OnClickListener
                    val index = otherIndex
                    otherIndex = -1
                    buttons[index]!!.state = Companion.STATE_NORMAL
                    var buttonDrawable =
                        if (buttons[index]!!.type == Companion.TYPE_LEFT) R.drawable.button_left else R.drawable.button_right
                    if (buttons[index]!!.type == Companion.TYPE_CENTER) buttonDrawable =
                        R.drawable.keyboardcenterbutton
                    buttons[index]!!.imgView!!.setImageBitmap(
                        imageManager!!
                            .loadImageFromResource(
                                buttonDrawable,
                                buttonConvertor.height,
                                buttonConvertor.width,
                                ImageManager.ScalingLogic.FIT
                            )
                    )
                    val fromX = +x - buttons[index]!!.x
                    val fromY = +y - buttons[index]!!.y
                    val toX = 0
                    val toY = 0
                    val translateAnimation = TranslateAnimation(
                        fromX.toFloat(), toX.toFloat(), fromY.toFloat(), toY.toFloat()
                    )
                    translateAnimation.fillAfter = true
                    translateAnimation.duration = 400
                    val scaleX = (answerConverter.width.toFloat()
                            / buttonConvertor.width)
                    val scaleY = (answerConverter.height.toFloat()
                            / buttonConvertor.height)
                    val scaleAnimation = ScaleAnimation(
                        scaleX, 1f, scaleY, 1f
                    )
                    scaleAnimation.fillAfter = true
                    scaleAnimation.duration = 400
                    val set = AnimationSet(true)
                    set.fillAfter = true
                    set.addAnimation(scaleAnimation)
                    set.addAnimation(translateAnimation)
                    buttons[index]!!.charTemp!!.startAnimation(set)
                })
                imgView!!.setOnTouchListener { v, event ->
                    if (event.action != MotionEvent.ACTION_UP) if (index == solution.replace(
                            "/",
                            ""
                        ).length - 1
                        || index == 0
                    ) {
                        val e = MotionEvent.obtain(event)
                        e.setLocation(
                            x + event.x, yaroConvertor!!.height - y
                                    + event.y
                        )
                        // if (event.getAction() == event.ACTION_DOWN)
                        swipeView.onTouchEvent(e)
                    }
                    false
                }
            } else if (mode == Companion.MODE_BUTTON) {
                imgView!!.setOnClickListener(OnClickListener {
                    val thisView = this@KeyView
                    if (thisView.state == Companion.STATE_TOUCHED
                        || thisView.state == Companion.STATE_REMOVED
                    ) return@OnClickListener
                    if (firstEmptyIndexAnswers != -1
                        || state == Companion.STATE_REMOVED
                    ) {
                        val mIndex = firstEmptyIndexAnswers
                        answers[mIndex]!!.otherIndex = index
                        otherIndex = mIndex
                        thisView.state = Companion.STATE_TOUCHED
                        var touchedDrawable =
                            if (thisView.type == Companion.TYPE_LEFT) R.drawable.button_left_touched else R.drawable.button_right_touched
                        if (thisView.type == Companion.TYPE_CENTER) touchedDrawable =
                            R.drawable.keyboardcenterbuttontouched
                        thisView.imgView!!.setImageBitmap(
                            imageManager!!
                                .loadImageFromResource(
                                    touchedDrawable,
                                    buttonConvertor.height,
                                    buttonConvertor.width,
                                    ImageManager.ScalingLogic.FIT
                                )
                        )
                        val toX = answers[mIndex]!!.x - x
                        val toY = answers[mIndex]!!.y - y
                        val translateAnimation = TranslateAnimation(
                            0f, toX.toFloat(), 0f, toY.toFloat()
                        )
                        translateAnimation.fillAfter = true
                        translateAnimation.duration = 400
                        val scaleX = (answerConverter.width.toFloat()
                                / buttonConvertor.width)
                        val scaleY = (answerConverter.height.toFloat()
                                / buttonConvertor.height)
                        val scaleAnimation = ScaleAnimation(
                            1f, scaleX, 1f, scaleY
                        )
                        scaleAnimation.fillAfter = true
                        scaleAnimation.duration = 400
                        if (!isCharTempAdded) {
                            val params = LayoutParams(
                                buttonConvertor.width,
                                buttonConvertor.height
                            )
                            params.leftMargin = x
                            params.topMargin = y
                            this@KeyboardView.addView(charTemp, params)
                            isCharTempAdded = true
                        }
                        val set = AnimationSet(true)
                        set.fillAfter = true
                        set.addAnimation(scaleAnimation)
                        set.addAnimation(translateAnimation)
                        charTemp!!.startAnimation(set)
                        if (isAllAnswered) if (onKeyboardEvent != null) onKeyboardEvent!!
                            .onAllAnswered(allAswers)
                    }
                })
            }
        }

        fun setText(character: Char?) {
            if (MODE_BUTTON != mode) {
                this.character = character
                return
            }
            charHolder!!.setImageBitmap(
                imageManager!!.loadImageFromResource(
                    getDrawable(character),
                    buttonConvertor.height, buttonConvertor.width,
                    ImageManager.ScalingLogic.FIT
                )
            )
            charTemp!!.setImageBitmap(
                imageManager!!.loadImageFromResource(
                    getDrawable(character),
                    buttonConvertor.height, buttonConvertor.width,
                    ImageManager.ScalingLogic.FIT
                )
            )
            this.character = character
        }

        fun setXY(x: Int, y: Int) {
            this.x = x
            this.y = y
        }

        fun getUnTocuhDrawableForButton(type: Int): Int {
            when (type) {
                Companion.TYPE_CENTER -> return R.drawable.keyboardcenterbutton
                Companion.TYPE_LEFT -> return R.drawable.button_left
                Companion.TYPE_RIGHT -> return R.drawable.button_right
            }
            return 0
        }

        fun getTocuhedDrawableForButton(type: Int): Int {
            when (type) {
                Companion.TYPE_CENTER -> return R.drawable.keyboardcenterbuttontouched
                Companion.TYPE_LEFT -> return R.drawable.button_left_touched
                Companion.TYPE_RIGHT -> return R.drawable.button_right_touched
            }
            return 0
        }


    }

    companion object {
        private const val TAG = "KeyboardView"
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
        const val TYPE_CENTER = 3
        const val MODE_ANSWER = 0
        const val MODE_BUTTON = 1
        const val MODE_LAMP = 3
        const val STATE_TOUCHED = 0
        const val STATE_NORMAL = 1
        const val STATE_EMPTY = 2
        const val STATE_REMOVED = 4
        const val STATE_ANSWERED = 8
    }

    fun getDrawable(s: Char?): Int {
        if (s == 'ص') return R.drawable.sad
        if (s == 'ه') return R.drawable.hah
        if (s == 'و') return R.drawable.vav
        if (s == 'پ') return R.drawable.pe
        if (s == 'د') return R.drawable.de
        if (s == 'ذ') return R.drawable.zee
        if (s == 'ر') return R.drawable.re
        if (s == 'ز') return R.drawable.ze
        if (s == 'ط') return R.drawable.ta
        if (s == 'ظ') return R.drawable.za
        if (s == 'ض') return R.drawable.zad
        if (s == 'ک') return R.drawable.ke
        if (s == 'ی') return R.drawable.ye
        if (s == 'س') return R.drawable.sin
        if (s == 'ب') return R.drawable.be
        if (s == 'ش') return R.drawable.shin
        if (s == 'گ') return R.drawable.ge
        if (s == 'ج') return R.drawable.j
        if (s == 'غ') return R.drawable.ghain
        if (s == 'خ') return R.drawable.khe
        if (s == 'ح') return R.drawable.he
        if (s == 'ل') return R.drawable.lam
        if (s == 'ف') return R.drawable.fe
        if (s == 'ا' || s == 'آ') return R.drawable.alef
        if (s == 'ق') return R.drawable.ghe
        if (s == 'چ') return R.drawable.che
        if (s == 'ث') return R.drawable.se
        if (s == 'ن') return R.drawable.non
        if (s == 'ع') return R.drawable.ayn
        if (s == 'ت') return R.drawable.te
        if (s == 'م') return R.drawable.mim
        return if (s == 'ژ') R.drawable.czhe else 0
    }


    init {
        var solution = solution
        solution = solution.replace(".", "/")
        this.solution = solution
        levelAnswer = solution.replace(" ", "").replace("/", "")
        val random = Random(System.currentTimeMillis())
        val string = solution.replace(" ", "").replace("/", "")
        val unshuffledavailableGuesses = ArrayList<Char>()
        for (charr in string.toCharArray()) unshuffledavailableGuesses.add(charr)
        while (unshuffledavailableGuesses.size != 21) {
            unshuffledavailableGuesses.add(
                allStrings[random
                    .nextInt(allStrings.length)]
            )
        }
        val availableGuesses = ArrayList<Char>()
        while (unshuffledavailableGuesses.size != 0) {
            availableGuesses.add(
                unshuffledavailableGuesses.removeAt(
                    random
                        .nextInt(unshuffledavailableGuesses.size)
                )
            )
        }
        buttons = arrayOfNulls<KeyView>(21)
        answers = arrayOfNulls(solution.replace("/", "").length)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        )
        val yaroConvertor = SizeConvertorFormHeight(
            sizeManager.deviceHeight.toDouble(), 1200, 1920, sizeManager
        )
        val baseWidth = if (yaroConvertor.width > sizeManager.deviceWidth) sizeManager
            .deviceWidth else yaroConvertor.width
        buttonConvertor = SizeConvertorFromWidth(
            baseWidth
                    * (10.6f / 100), 120, 132, sizeManager
        )
        answerConverter = SizeConvertorFromWidth(
            baseWidth
                    * (8.3f / 100), 77, 77, sizeManager
        )
        swipeView = SwipeView(context)
        heightSwipe = (yaroConvertor.height * 0.18).toInt()
        val swipeParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            (yaroConvertor.height * 0.18).toInt()
        )
        swipeParams.topMargin = (yaroConvertor.height * 0)
        addView(swipeView, swipeParams)
        val lengthManager = LengthManager(getContext())
        mReaminingLenght =
            (sizeManager.deviceHeight - lengthManager.headerHeight - lengthManager.getLevelImageFrameHeight()).toDouble()

//        Logger.d("TAG", "whole" + SizeManager.getScreenHeight());
//        Logger.d("TAG", "reamaning " + mReaminingLenght);
//        Logger.d("TAG", "header " + lengthManager.getHeaderHeight());
//        Logger.d("TAG", "frame " + lengthManager.getLevelImageFrameHeight());
        val wholeButtonsLength = 3.2 * buttonConvertor.height
        mMargin = ((mReaminingLenght - (wholeButtonsLength + answerConverter.height)) / 3.0).toInt()
        if (solution.contains("/")) {
            mMargin = ((mReaminingLenght - (wholeButtonsLength +
                    answerConverter.height * 2.1)) / 3.0).toInt()
            getAnswersInALine(solution.split("/").toTypedArray()[0], context, true, 1, 0)
            getAnswersInALine(
                solution.split("/").toTypedArray()[1], context, true, 2,
                solution.split("/").toTypedArray()[0].length
            )
        } else {
            getAnswersInALine(solution, context, false, 1, 0)
        }
        leftX = (answers[solution.replace("/", "").length - 1]!!.x
                + answerConverter.width)
        rightX = answers[0]!!.x - answerConverter.width
        val buttonContainer = RelativeLayout(context)
        val buttonContainerParams = LayoutParams(layoutParams)
        var middle: Int
        var top: Int
        var offSet: Int
        if (solution.contains("/")) buttonContainerParams.topMargin =
            (2 * mMargin + 2.1 * answerConverter.height).toInt() else buttonContainerParams.topMargin =
            2 * mMargin + answerConverter.height
        top = buttonContainerParams.topMargin
        var j = 0
        var k = 0
        for (i in 0..3) {
            middle = sizeManager.deviceWidth / 2
            offSet = buttonConvertor.width / 30
            val paramsRight = LayoutParams(
                layoutParams
            )
            val paramsLeft = LayoutParams(
                layoutParams
            )
            paramsLeft.leftMargin = middle - offSet - ((i + 1)
                    * buttonConvertor.width)
            paramsRight.leftMargin = middle + offSet + (i
                    * buttonConvertor.width)
            val rightKey = KeyView(
                context,
                if (i != 3) Companion.TYPE_CENTER else Companion.TYPE_RIGHT,
                Companion.MODE_BUTTON,
                availableGuesses[k++]
            )
            val leftKey = KeyView(
                context, if (i != 3) Companion.TYPE_CENTER else Companion.TYPE_LEFT,
                Companion.MODE_BUTTON, availableGuesses[k++]
            )
            rightKey.setXY(paramsRight.leftMargin, top)
            leftKey.setXY(paramsLeft.leftMargin, top)
            buttonContainer.addView(leftKey, paramsLeft)
            buttonContainer.addView(rightKey, paramsRight)
            buttons[j++] = rightKey
            buttons[j - 1]!!.index = j - 1
            buttons[j++] = leftKey
            buttons[j - 1]!!.index = j - 1
        }
        for (i in 0..2) {
            middle = sizeManager.deviceWidth / 2
            offSet = buttonConvertor.width / 2
            val paramsRight = LayoutParams(
                layoutParams
            )
            val paramsLeft = LayoutParams(
                layoutParams
            )
            paramsRight.topMargin = buttonConvertor
                .height + buttonConvertor.height / 10
            paramsLeft.topMargin = paramsRight.topMargin
            paramsLeft.leftMargin = middle - offSet - ((i + 1)
                    * buttonConvertor.width)
            paramsRight.leftMargin = middle + offSet + (i
                    * buttonConvertor.width)
            top = paramsLeft.topMargin + buttonContainerParams.topMargin
            val rightKey = KeyView(
                context,
                if (i != 2) Companion.TYPE_CENTER else Companion.TYPE_RIGHT,
                Companion.MODE_BUTTON,
                availableGuesses[k++]
            )
            val leftKey = KeyView(
                context, if (i != 2) Companion.TYPE_CENTER else Companion.TYPE_LEFT,
                Companion.MODE_BUTTON, availableGuesses[k++]
            )
            rightKey.setXY(paramsRight.leftMargin, top)
            leftKey.setXY(paramsLeft.leftMargin, top)
            buttonContainer.addView(leftKey, paramsLeft)
            buttonContainer.addView(rightKey, paramsRight)
            buttons[j++] = rightKey
            buttons[j - 1]!!.index = j - 1
            buttons[j++] = leftKey
            buttons[j - 1]!!.index = j - 1
        }
        val mmiddle = sizeManager.deviceWidth / 2
        val moffSet = buttonConvertor.width / 2
        val paramsLamp = LayoutParams(
            layoutParams
        )
        paramsLamp.topMargin = (buttonConvertor.height
                + buttonConvertor.height / 10)
        val lamp = KeyView(
            context, Companion.TYPE_CENTER,
            Companion.MODE_BUTTON, availableGuesses[k++]
        )
        paramsLamp.leftMargin = mmiddle - moffSet
        lamp.setXY(paramsLamp.leftMargin, paramsLamp.topMargin + buttonContainerParams.topMargin)
        buttonContainer.addView(lamp, paramsLamp)
        buttons[j++] = lamp
        buttons[j - 1]!!.index = j - 1
        for (i in 0..2) {
            middle = sizeManager.deviceWidth / 2
            offSet = buttonConvertor.width / 30
            val paramsRight = LayoutParams(
                layoutParams
            )
            val paramsLeft = LayoutParams(
                layoutParams
            )
            paramsRight.topMargin = buttonConvertor
                .height * 2 + buttonConvertor.height / 5
            paramsLeft.topMargin = paramsRight.topMargin
            top = paramsLeft.topMargin + buttonContainerParams.topMargin
            paramsLeft.leftMargin = middle - offSet - ((i + 1)
                    * buttonConvertor.width)
            paramsRight.leftMargin = middle + offSet + (i
                    * buttonConvertor.width)
            val rightKey = KeyView(
                context,
                if (i != 2) Companion.TYPE_CENTER else Companion.TYPE_RIGHT,
                Companion.MODE_BUTTON,
                availableGuesses[k++]
            )
            val leftKey = KeyView(
                context, if (i != 2) Companion.TYPE_CENTER else Companion.TYPE_LEFT,
                Companion.MODE_BUTTON, availableGuesses[k++]
            )
            rightKey.setXY(paramsRight.leftMargin, top)
            leftKey.setXY(paramsLeft.leftMargin, top)
            buttonContainer.addView(leftKey, paramsLeft)
            buttonContainer.addView(rightKey, paramsRight)
            buttons[j++] = rightKey
            buttons[j - 1]!!.index = j - 1
            buttons[j++] = leftKey
            buttons[j - 1]!!.index = j - 1
        }
        addView(buttonContainer, buttonContainerParams)
    }
}