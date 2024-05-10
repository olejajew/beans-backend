package com.downappload.beanlogger.data

import com.downappload.beanlogger.utils.Bean


data class PlayerWrapper(
    val player: Player,
    var fields: MutableList<Field> = mutableListOf()
) {

    fun init(fieldsCount: Int) {
        for (i in 0 until fieldsCount) {
            fields.add(Field(i))
        }
    }

    fun setWithoutProblem(bean: Bean, count: Int): Int? {
        val alreadySet = fields.firstOrNull { it.beanType == bean }?.apply {
            this.addCards(count)
        }
        if(alreadySet != null){
            return alreadySet.index
        }
        val setOnEmpty = fields.firstOrNull{it.beanType == Bean.FREE}?.apply {
            this.set(bean, count)
        }
        return setOnEmpty?.index
    }

    fun setToField(field: Field, bean: Bean, count: Int) {
        fields[field.index].set(bean, count)
    }

}