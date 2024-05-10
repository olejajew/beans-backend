package com.downappload.beanlogger.ui.components

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.ui.IGameActivity

class LookAtFieldComponent(
    title: String,
    private val iGameActivity: IGameActivity,
    private val onDone: () -> Unit
) : BaseComponent(
    iGameActivity.getContext(),
    title,
    iGameActivity,
) {

    override fun getView(player: Player): View {
        val baseLayout = createEmptyLinearLayout(LinearLayout.VERTICAL)
        iGameActivity.getGameProvider().getPlayers().forEach { targetPlayer ->
            val layout = createEmptyLinearLayout(LinearLayout.VERTICAL).apply {
                addView(createTextView(targetPlayer.name, "#000000"))
                addView(createRowForPlayer(targetPlayer))
            }
            baseLayout.addView(layout)
        }
        return baseLayout
    }

    private fun createRowForPlayer(player: Player): LinearLayout {
        val fields = iGameActivity.getGameProvider().getFields(player)
        val baseLayout = createEmptyLinearLayout(LinearLayout.HORIZONTAL)
        fields.sortedBy { it.index }.onEach { field ->
            val count = createTextView(field.count.toString(), "#ffffff", 48f)
            val manipulativeButton = createManipulativeCard(
                field.beanType.image,
                count,
                onIncrement = {
                    onDone()
                    true
                }, onDecrement = {
                    onDone
                    true
                }
            )
            baseLayout.addView(manipulativeButton)
        }
        return baseLayout
    }

    override fun clearData() {

    }

    override fun goNext() {

    }
}