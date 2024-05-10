package com.downappload.beanlogger.ui.components

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.ui.IGameActivity

class PlayerComponent(
    title: String,
    private val iGameActivity: IGameActivity,
    private val onPlayerSelected: (player: Player) -> Unit
) : BaseComponent(
    iGameActivity.getContext(),
    title,
    iGameActivity
) {


    override fun getView(player: Player): View {
        val baseLinearLayout = createEmptyLinearLayout(LinearLayout.VERTICAL)
        val players = iGameActivity.getGameProvider().getPlayers(player)
        players.forEach { otherPlayers ->
            val textView = createTextView(otherPlayers.name.capitalize(), otherPlayers.titleColor, setTextSize = 48f)
            val cardView = createCustomCardView(otherPlayers.backgroundColor, textView) {
                onPlayerSelected(otherPlayers)
            }
            baseLinearLayout.addView(cardView)
        }
        return baseLinearLayout
    }

    override fun clearData() {

    }

    override fun goNext() {
        TODO("Not yet implemented")
    }
}