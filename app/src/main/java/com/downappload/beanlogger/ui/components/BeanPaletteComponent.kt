package com.downappload.beanlogger.ui.components

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.ui.IGameActivity
import com.downappload.beanlogger.utils.Bean

class BeanPaletteComponent(
    title: String, iGameActivity: IGameActivity,
    private val onSelected: (HashMap<Bean, Int>) -> Unit
) :
    BaseComponent(iGameActivity.getContext(), title, iGameActivity) {

    private val selectedBeans = hashMapOf<Bean, Int>()

    private val palette = listOf(
        listOf(Bean.CacaoBean, Bean.GardenBean, Bean.RedBean),
        listOf(Bean.BlackEyedBean, Bean.SoyBean, Bean.GreenBean),
        listOf(Bean.StinkBean, Bean.ChilliBean, Bean.BlueBean, ),
        listOf(Bean.WaxBean, Bean.CoffeeBean, null)
    )

    override fun getView(player: Player): View {
        val linearLayout = createEmptyLinearLayout(LinearLayout.VERTICAL)
        palette.forEach { row ->
            val rowLayout = createEmptyLinearLayout(LinearLayout.HORIZONTAL)
            row.forEach {
                if(it != null) {
                    rowLayout.addView(createBeanCard(it))
                } else {
                    rowLayout.addView(createEmptyFrameLayout())
                }
            }
            linearLayout.addView(rowLayout)
        }
        return linearLayout
    }

    override fun clearData() {
        selectedBeans.clear()
    }

    override fun goNext() {
        onSelected(selectedBeans)
    }

    private fun createBeanCard(bean: Bean): FrameLayout {
        val textView = createTextView("0", "#ffffff", 48f)

        return createManipulativeCard(bean.image, textView, onIncrement = {
            val amount = (selectedBeans.get(bean) ?: 0) + 1
            selectedBeans[bean] = amount
            textView.setText(amount.toString())
            true
        }, onDecrement = {
            val amount = (selectedBeans.get(bean) ?: 0) - 1
            if (amount < 1) {
                selectedBeans.remove(bean)
                textView.setText("0")
                true
            } else {
                selectedBeans[bean] = amount
                textView.setText(amount.toString())
                false
            }
        })
    }

}