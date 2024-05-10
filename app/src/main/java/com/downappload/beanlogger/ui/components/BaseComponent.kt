package com.downappload.beanlogger.ui.components

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.ui.IGameActivity
import com.downappload.beanlogger.utils.hide
import com.downappload.beanlogger.utils.show

abstract class BaseComponent(
    val context: Context,
    title: String,
    private val iGameActivity: IGameActivity,
) {

    init{
        iGameActivity.setTitle(title)
    }

    fun proceed(){
        if (checkNotNeed()) {
            goNext()
            return
        }
    }

    fun reCreate() {
        clearData()
        iGameActivity.setComponent(this)
    }

    fun createCustomCardView(
        backgroundColor: String,
        centralView: View,
        onClickListener: OnClickListener,
    ): CardView {
        // Создание объекта CardView
        val cardView = CardView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, // ширина
                FrameLayout.LayoutParams.MATCH_PARENT // высота
            ).also {
                it.gravity = Gravity.CENTER
                it.weight = 1f
                it.setMargins(16, 16, 16, 16) // отступы
            }
            setBackgroundColor(Color.parseColor(backgroundColor)) // цвет фона
            radius = 16f // радиус скругления углов
            cardElevation = 8f // высота тени
        }

        cardView.addView(centralView)
        cardView.setOnClickListener(onClickListener)

        return cardView
    }

    fun createTextView(title: String, textColor: String, setTextSize: Float = 16f): TextView {
        return TextView(context).apply {
            text = title // текст
            setTextColor(Color.parseColor(textColor)) // цвет текста
            textSize = setTextSize // размер текста
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }
    }


    fun createEmptyFrameLayout(setWeight: Float = 1f) = FrameLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
        ).apply {
            weight = setWeight
        }
    }

    fun createEmptyLinearLayout(setOrientation: Int) = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
        ).apply {
            orientation = setOrientation
            weight = 1f
        }
    }

    fun createImageView(imageResource: Int): ImageView {
        return ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setImageResource(imageResource)
        }
    }

    fun createManipulativeCard(
        image: Int,
        textViewIfNeed: TextView?,
        onIncrement: () -> Boolean,
        onDecrement: () -> Boolean,
    ): FrameLayout {
        val frameLayout = createEmptyFrameLayout()

        val imageView = createImageView(image)
        val cover = createEmptyFrameLayout().apply {
            setBackgroundColor(Color.parseColor("#4caf50"))
            hide()
        }
        val manipulativeButtons = createManipulatedButtons(onAdd = {
            val needToShowCover = onIncrement()
            if(needToShowCover){
                cover.show(0.5f)
            }
        }, onRemove = {
            val needToHideCover = onDecrement()
            if(needToHideCover){
                cover.hide()
            }
        })

        frameLayout.addView(imageView)
        frameLayout.addView(cover)
        if (textViewIfNeed != null) {
            frameLayout.addView(textViewIfNeed)
        }
        frameLayout.addView(manipulativeButtons)

        return frameLayout
    }

    private fun createManipulatedButtons(onAdd: () -> Unit, onRemove: () -> Unit): View {
        val layout = createEmptyLinearLayout(LinearLayout.HORIZONTAL)
        val buttonRemove = createEmptyFrameLayout(3f).apply {
            setOnClickListener {
                onRemove()
            }
        }
        val buttonAdd = createEmptyFrameLayout(2f).apply {
            setOnClickListener {
                onAdd()
            }
        }
        layout.addView(buttonRemove)
        layout.addView(buttonAdd)

        return layout
    }

    abstract fun getView(player: Player): View

    abstract fun clearData()

    abstract fun goNext()

    open fun checkNotNeed(): Boolean = false

}