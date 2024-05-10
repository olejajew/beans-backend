package com.downappload.beanlogger.ui

import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.ui.components.ActionWidgetComponent
import com.downappload.beanlogger.ui.components.BaseComponent
import com.downappload.beanlogger.ui.components.BeanPaletteComponent
import com.downappload.beanlogger.ui.components.FieldComponent
import com.downappload.beanlogger.ui.components.LookAtFieldComponent
import com.downappload.beanlogger.ui.components.PlayerComponent
import com.downappload.beanlogger.ui.components.ReasonComponent
import com.downappload.beanlogger.utils.ActionType

class GameFlowProvider(
    private val iGameActivity: IGameActivity
) {

    private val gameProvider = iGameActivity.getGameProvider()

    fun roundFlow(player: Player, action: ActionType) {
        val component: BaseComponent = when (action) {
            ActionType.crop_field -> cropField(player)
            ActionType.set_beans -> setBeans(player)
            ActionType.open_cards -> openCards(player)
            ActionType.exchange -> exchange(player)
            ActionType.get -> iGetBeans(player)
            ActionType.give -> iGiveBeans(player)
            ActionType.look_at_field -> lookAtField(player)
            ActionType.step_end -> {
                gameProvider.stepEnd(player)
                null
            }
        } ?: return iGameActivity.restartFlow()
        iGameActivity.setComponent(component)
    }

    private fun setBeans(player: Player): BeanPaletteComponent {
        return BeanPaletteComponent(
            "${player.name} высаживает...",
            iGameActivity
        ) { beansToSet ->
            iGameActivity.setComponent(
                FieldComponent(
                    "Высаживает на...",
                    gameProvider.notHavePlaceFor(
                        player, beansToSet
                    ).count(),
                    iGameActivity
                ) { fieldsToCrop ->
                    gameProvider.playerSetBeans(player, beansToSet, fieldsToCrop)
                    iGameActivity.restartFlow()
                }
            )
        }
    }

    private fun cropField(player: Player): BaseComponent {
        return FieldComponent("Я срезаю", 3, iGameActivity) { choicedFields ->
            gameProvider.playerCropField(player, choicedFields)
            iGameActivity.restartFlow()
        }
    }

    private fun lookAtField(player: Player): LookAtFieldComponent {
        return LookAtFieldComponent("Что у нас тут...", iGameActivity) {
            iGameActivity.restartFlow()
        }
    }

    private fun openCards(player: Player): BeanPaletteComponent {
        return BeanPaletteComponent("Я открываю на стол...", iGameActivity) { beans ->
            iGameActivity.getGameProvider().openCardsToDeck(player, beans)
            //Выкладываем на поле

            iGameActivity.restartFlow()
        }
    }

    private fun exchange(iAm: Player): BeanPaletteComponent {
        return BeanPaletteComponent("${iAm.name} отдает...", iGameActivity) { iGive ->
            iGameActivity.setComponent(
                PlayerComponent("${iAm.name} отдает ...", iGameActivity) { heIs ->
                    iGameActivity.setComponent(
                        BeanPaletteComponent("${heIs.name} отдает...", iGameActivity) { heGive ->
                            iGameActivity.setComponent(
                                FieldComponent(
                                    "${iAm.name} высаживает на...",
                                    iGameActivity.getGameProvider()
                                        .notHavePlaceFor(iAm, heGive).size,
                                    iGameActivity
                                ) { myFieldsToCrop ->
                                    iGameActivity.setComponent(
                                        FieldComponent(
                                            "${heIs.name} высаживает на...",
                                            iGameActivity.getGameProvider()
                                                .notHavePlaceFor(heIs, iGive).size,
                                            iGameActivity
                                        ) { hisFieldsToCrop ->
                                            gameProvider.exchange(
                                                iAm,
                                                iGive,
                                                heIs,
                                                heGive,
                                                myFieldsToCrop,
                                                hisFieldsToCrop,
                                            )
                                            iGameActivity.restartFlow()
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    }

    fun iGiveBeans(iAm: Player): PlayerComponent {
        return PlayerComponent("${iAm.name} отдает...", iGameActivity) { heIs ->
            iGameActivity.setComponent(
                BeanPaletteComponent("${iAm.name} отдает...", iGameActivity) { iGive ->
                    iGameActivity.setComponent(
                        ReasonComponent("Так как... ", iGameActivity) { reason ->
                            iGameActivity.setComponent(
                                FieldComponent(
                                    "${heIs.name} готов срезать...",
                                    iGameActivity.getGameProvider().notHavePlaceFor(heIs, iGive)
                                        .count(),
                                    iGameActivity
                                ) { iReadyToCrop ->

                                    gameProvider.iGive(iAm, iGive, heIs, reason, iReadyToCrop)
                                    iGameActivity.restartFlow()
                                }
                            )
                        }
                    )
                }
            )
        }
    }

    private fun iGetBeans(iAm: Player): PlayerComponent {
        return PlayerComponent("${iAm.name} получает от...", iGameActivity) { heIs ->
            iGameActivity.setComponent(
                BeanPaletteComponent("${heIs.name} отдает...", iGameActivity) { heGives ->
                    iGameActivity.setComponent(
                        ReasonComponent("Так как... ", iGameActivity) { reason ->
                            iGameActivity.setComponent(
                                FieldComponent(
                                    "${iAm.name} готов срезать...",
                                    iGameActivity.getGameProvider().notHavePlaceFor(iAm, heGives)
                                        .count(),
                                    iGameActivity
                                ) { iReadyToCrop ->

                                    gameProvider.iGet(iAm, heGives, heIs, reason, iReadyToCrop)
                                    iGameActivity.restartFlow()
                                }
                            )
                        }
                    )
                }
            )
        }
    }

}

