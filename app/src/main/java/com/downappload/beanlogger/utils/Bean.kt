package com.downappload.beanlogger.utils

import com.downappload.beanlogger.R

enum class Bean(
    val id: Int,
    val title: String,
    val image: Int,
) {
    FREE(0, "FREE", R.drawable.field),
    CacaoBean(1, "Cacao bean", R.drawable.bean_cacao),
    GardenBean(2, "Garden bean", R.drawable.bean_garden),
    RedBean(3, "Red bean", R.drawable.bean_red),
    BlackEyedBean(4, "Black eyed bean", R.drawable.bean_black_eyed),
    SoyBean(5, "Soy bean", R.drawable.bean_soy),
    GreenBean(6, "Green bean", R.drawable.bean_green),
    StinkBean(7, "Stink bean", R.drawable.bean_stink),
    ChilliBean(8, "Chilli bean", R.drawable.bean_chilli),
    BlueBean(9, "Blue bean", R.drawable.bean_blue),
    WaxBean(10, "Wax bean", R.drawable.bean_wax),
    CoffeeBean(11, "Coffee bean", R.drawable.bean_coffee);

    companion object{
        fun activeBeans() = values().filter { it != FREE }
    }
}