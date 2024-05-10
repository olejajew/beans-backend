package com.downappload.beanlogger.data

import com.downappload.beanlogger.utils.Bean

data class Field(
    var index: Int = 0,
    var count: Int = 0,
    var beanType: Bean = Bean.FREE
) {

    fun clear() {
        count = 0
        beanType = Bean.FREE
    }

    fun set(bean: Bean, count: Int) {
        if(bean != beanType){
            clear()
        }
        beanType = bean
        this.count += count
    }

    fun addCards(count: Int) {
        this.count += count
    }

}