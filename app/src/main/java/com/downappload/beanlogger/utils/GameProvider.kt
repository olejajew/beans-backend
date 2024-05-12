package com.downappload.beanlogger.utils

import android.content.Context
import android.util.Log
import com.downappload.beanlogger.AppClass
import com.downappload.beanlogger.data.Field
import com.downappload.beanlogger.data.Player
import com.downappload.beanlogger.data.PlayerWrapper
import com.downappload.beanlogger.data.Reason
import com.downappload.beanlogger.data.database.DatabaseAdapter
import com.downappload.beanlogger.data.database.entities.LogEntity
import com.downappload.beanlogger.data.rest.ApiLogger
import com.downappload.beanlogger.utils.dumps.Dump
import com.downappload.beanlogger.utils.dumps.DumpSaver
import com.google.gson.Gson
import java.lang.Exception

class GameProvider(context: Context) {

    private val databaseAdapter = DatabaseAdapter.getInstance(context)

    val playerWrappers = mutableMapOf<Int, PlayerWrapper>()

    private val dumpSaver = DumpSaver(this, context)

    private val gson = Gson()

    private val activePlayerProvider: ActivePlayerProvider by lazy {
        ActivePlayerProvider(
            getPlayers()
        )
    }

    init {
        broadcast(mapOf("action" to "new_game"))
    }


    fun startGame() {
        val dump = dumpSaver.getDump()
        if (dump != null) {
            restoreGame(dump)
        } else {
            defaultInit()
        }
    }

    private fun restoreGame(dump: Dump) {
        val players = dump.players
        playerWrappers.clear()
        players.forEach {
            playerWrappers[it.player.id] = it
        }
        activePlayerProvider.setActivePlayer(dump.activePlayerId)
    }

    private fun defaultInit() {
        val players = listOf(
            Player(0, "Олежа", "#000000", "#ff5722"),
            Player(1, "Рина", "#000000", "#4caf50"),
            Player(2, "Вероника", "#ffffff", "#9c27b0")
        )
        playerWrappers.clear()
        players.forEach {
            playerWrappers[it.id] = PlayerWrapper(it).apply {
                this.init(3)
            }
        }
    }


    private fun broadcast(
        pair: Map<String, Any>,
    ) {
        Log.i("broadcast()", pair.toString())
        val messageString = gson.toJson(pair)
        databaseAdapter.getLogDao().saveLog(LogEntity(data = messageString))
        ApiLogger.saveLog(messageString)
        val dump = dumpSaver.createDump()
        if (dump != null) {
            ApiLogger.saveDump(gson.toJson(dump))
        }
    }

    fun getFields(player: Player) = playerWrappers[player.id]!!.fields

    fun getPlayers(except: Player? = null): MutableList<Player> =
        playerWrappers.values.filter { it.player != except }.map { it.player }.toMutableList()

    fun playerSetBeans(player: Player, beans: Map<Bean, Int>, fields: List<Field>) {
        if (beans.isEmpty()) {
            return
        }
        val fieldsForCrop = fields.toMutableList()

        try {
            playerWrappers[player.id]!!.apply {
                beans.forEach { t, u ->
                    val setToHisField = this.setWithoutProblem(t, u)
                    if (setToHisField != null) {
                        //Все посажено и хорошо
                        broadcast(
                            mapOf(
                                "action" to ActionType.set_beans,
                                "player" to player,
                                "fieldIndex" to setToHisField,
                                "supData" to (t to u)
                            )
                        )
                    } else {
                        val field = fieldsForCrop.removeAt(0)
                        playerCropField(player, listOf(field))
                        this.setToField(field, t, u)
                        broadcast(
                            mapOf(
                                "action" to ActionType.set_beans,
                                "fieldIndex" to field.index,
                                "player" to player,
                                "supData" to (t to u)
                            )
                        )
                    }
                }

            }
        } catch (e: Exception) {
            AppClass.getInstance()
                .showToast("Ошибка. У ${player.name} мало полей для посадки: ${fieldsForCrop.size}")
            e.printStackTrace()
        }
    }

    fun playerCropField(player: Player, fields: List<Field>) {
        for (field in fields) {
            playerWrappers[player.id]!!.fields.first { it.index == field.index }.apply {
                val count = this.count
                val bean = this.beanType
                broadcast(
                    mapOf(
                        "action" to ActionType.crop_field,
                        "fieldIndex" to field.index,
                        "supData" to (bean to count),
                        "player" to player,
                        )
                )
                this.clear()
            }
        }
    }

    fun exchange(
        iAm: Player,
        iGive: Map<Bean, Int>,
        heIs: Player,
        heGive: Map<Bean, Int>,
        forNotMyBeans: List<Field>,
        forNotHisBeans: List<Field>
    ) {
        playerSetBeans(iAm, heGive, forNotMyBeans)
        playerSetBeans(heIs, iGive, forNotHisBeans)

        broadcast(
            mapOf(
                "action" to ActionType.exchange,
                "playerFirst" to iAm.id,
                "supFirstGot" to (heGive.map { it.key to it.value }),
                "supFirstFields" to (forNotMyBeans.map { it.index }),
                "playerSecond" to heIs.id,
                "subSecondGot" to (iGive.map { it.key to it.value }),
                "supSecondFields" to (forNotHisBeans.map { it.index }),
            )
        )
    }

    fun notHavePlaceFor(player: Player, heGive: Map<Bean, Int>): Map<Bean, Int> {
        val beansOnFields = playerWrappers[player.id]!!.fields.map { it.beanType }
        val heNeed = heGive.filter { !beansOnFields.contains(it.key) }
        val emptyFields = beansOnFields.filter { it == Bean.FREE }
        if (emptyFields.size >= heNeed.size) {
            return emptyMap()
        }
        return heNeed
    }

    fun iGive(
        iAm: Player,
        iGive: Map<Bean, Int>,
        heIs: Player,
        reason: Reason,
        hisFieldsForCrop: List<Field>
    ) {
        playerSetBeans(heIs, iGive, hisFieldsForCrop)

        broadcast(
            mapOf(
                "action" to ActionType.give,
                "iAm" to iAm.name,
                "iGive" to iGive,
                "heIs" to heIs,
                "reason" to reason
            )
        )
    }

    fun iGet(
        iAm: Player,
        heGive: Map<Bean, Int>,
        heIs: Player,
        reason: Reason,
        myFieldsToCrop: List<Field>
    ) {
        playerSetBeans(iAm, heGive, myFieldsToCrop)

        broadcast(
            mapOf(
                "action" to ActionType.get,
                "iAm" to iAm.name,
                "iGive" to heGive,
                "heIs" to heIs,
                "reason" to reason
            )
        )
    }

    fun openCardsToDeck(player: Player, bean: Map<Bean, Int>) {
        broadcast(
            mapOf(
                "action" to ActionType.open_cards,
                "beans" to bean,
                "player" to player
            )
        )
    }

    fun getActivePlayer(): Player = activePlayerProvider.getActivePlayer()

    fun stepEnd(player: Player, gotCards: Int = 3) {
        activePlayerProvider.stepEnd()
        broadcast(
            mapOf(
                "action" to "got_from_deck",
                "player" to player.name,
                "count" to gotCards
            )
        )
    }

    fun clearSession() {
        dumpSaver.deleteOldDumps()
        defaultInit()
        activePlayerProvider.setActivePlayer(0)
        broadcast(mapOf("action" to "drop_and_new_game"))

    }

}