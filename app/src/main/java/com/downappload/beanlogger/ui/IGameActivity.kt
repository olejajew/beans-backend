package com.downappload.beanlogger.ui

import android.content.Context
import com.downappload.beanlogger.ui.components.BaseComponent
import com.downappload.beanlogger.utils.GameProvider

interface IGameActivity {

    fun setComponent(baseComponent: BaseComponent)

    fun restartFlow()

    fun setTitle(title: String)

    fun getGameProvider(): GameProvider

    fun getContext(): Context

    fun clearSessionRequest()
}