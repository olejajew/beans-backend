package com.downappload.beanlogger.ui.components

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.ui.IGameActivity
import com.downappload.beanlogger.utils.ActionType

class ActionWidgetComponent(
    playerName: String,
    iGameActivity: IGameActivity,
    private val onActionSelected: (ActionType) -> Unit
) : BaseComponent(iGameActivity.getContext(), "$playerName ходит...", iGameActivity) {

    private val palette = listOf(
        listOf(
            ActionType.crop_field, ActionType.open_cards,
        ),
        listOf(ActionType.set_beans),
        listOf(ActionType.exchange),
        listOf(ActionType.get, ActionType.give),
        listOf(ActionType.look_at_field, ActionType.step_end)
    )

    override fun getView(player: Player): View {
        val baseLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        palette.forEach { row ->
            val rowLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    weight = 1f
                }
                row.forEach { action ->
                    this.addView(
                        createCustomCardView(
                            action.color,
                            createTextView(action.title, action.textColor)
                        ) {
                            onActionSelected(action)
                        }
                    )
                }
            }
            baseLayout.addView(rowLayout)
        }

        return baseLayout
    }

    override fun clearData() {
    }

    override fun goNext() {
    }

}