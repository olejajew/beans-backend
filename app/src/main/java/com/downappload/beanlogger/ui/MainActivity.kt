package com.downappload.beanlogger.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.downappload.beanlogger.R
import com.downappload.beanlogger.ui.components.ActionWidgetComponent
import com.downappload.beanlogger.ui.components.BaseComponent
import com.downappload.beanlogger.utils.GameProvider

class MainActivity : AppCompatActivity(R.layout.activity_main), IGameActivity {

    private val mGameProvider: GameProvider by lazy {
        GameProvider(this).apply {
            this.startGame()
        }
    }

    private val mGameFlowProvider: GameFlowProvider by lazy {
        GameFlowProvider(this)
    }

    private var baseComponent: BaseComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.fb_next).setOnClickListener {
            baseComponent?.goNext()
        }

        findViewById<View>(R.id.fb_clear).setOnClickListener {
            baseComponent?.reCreate()
        }

        restartFlow()
    }

    override fun setComponent(baseComponent: BaseComponent) {
        this.baseComponent = baseComponent
        getContainer().apply {
            this.removeAllViews()
            addView(
                baseComponent.getView(player = mGameProvider.getActivePlayer())
            )
        }
        baseComponent.proceed()
    }

    private fun getContainer(): ViewGroup = findViewById(R.id.fl_container)

    override fun getGameProvider() = mGameProvider

    override fun getContext(): Context = this

    override fun restartFlow() {
        val activePlayer = mGameProvider.getActivePlayer()
        setComponent(ActionWidgetComponent(activePlayer.name, this) { action ->
            mGameFlowProvider.roundFlow(activePlayer, action)
        })
    }

    override fun setTitle(title: String) {
        findViewById<TextView>(R.id.tv_title).setText(title)
    }

    override fun onBackPressed() {
        restartFlow()
    }
}