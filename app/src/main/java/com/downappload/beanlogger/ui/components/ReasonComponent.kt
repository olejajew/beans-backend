package com.downappload.beanlogger.ui.components

import android.view.View
import android.widget.LinearLayout
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.data.Reason
import com.downappload.beanlogger.ui.IGameActivity

class ReasonComponent(
    title: String,
    iGameActivity: IGameActivity,
    private val onReasonSelected: (Reason) -> Unit
) : BaseComponent(iGameActivity.getContext(), title, iGameActivity) {

    override fun getView(player: Player): View {
        val baseLayout = createEmptyLinearLayout(LinearLayout.VERTICAL)
        Reason.values().forEach { reason ->
            val text = createTextView(reason.title, reason.textColor, 24f)
            val card = createCustomCardView(reason.backgroundColor, text){
                onReasonSelected(reason)
            }
            baseLayout.addView(card)
        }
        return baseLayout
    }

    override fun clearData() {
    }

    override fun goNext() {
        onReasonSelected(Reason.not_sure)
    }
}