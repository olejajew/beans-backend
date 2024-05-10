package com.downappload.beanlogger.ui.components

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.downappload.beanlogger.data.Field
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.ui.IGameActivity

class FieldComponent(
    title: String,
    private val fieldsCount: Int,
    private val iGameActivity: IGameActivity,
    private val onSelected: (List<Field>) -> Unit
) : BaseComponent(
    iGameActivity.getContext(),
    title,
    iGameActivity
) {

    private val selectedFields = mutableSetOf<Field>()

    override fun getView(player: Player): View {
        val baseLayout = createEmptyLinearLayout(LinearLayout.VERTICAL)
        val fields = iGameActivity.getGameProvider().getFields(player)
        fields.sortedBy { it.index }.onEach {field ->
            val count = createTextView(field.count.toString(), "#ffffff", 48f)
            val manipulativeButton = createManipulativeCard(
                field.beanType.image,
                count,
                onIncrement = {
                    selectedFields.add(field)
                    checkDone()
                    true
                }, onDecrement = {
                    selectedFields.remove(field)
                    true
                }
            )
            baseLayout.addView(manipulativeButton)
        }
        return baseLayout
    }

    private fun checkDone(){
        if(selectedFields.count() == fieldsCount){
            goNext()
        }
    }

    override fun clearData() {
        selectedFields.clear()
    }

    override fun goNext() {
        onSelected(selectedFields.toMutableList())
    }

    override fun checkNotNeed(): Boolean {
        Log.e("!!!", "Field count: $fieldsCount")
        return fieldsCount == 0
    }

}