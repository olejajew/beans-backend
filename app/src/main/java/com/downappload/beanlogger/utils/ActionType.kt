package com.downappload.beanlogger.utils

enum class ActionType(val title: String, val color: String, val textColor: String) {
    set_beans("Высаживает", "#4CAF50", "#ffffff"),
    crop_field("Срезает бобы", "#546E7A", "#ffffff"),
    open_cards("Открывает", "#E0E0E0", "#000000"),
    exchange("Меняет", "#FFC107", "#000000"),
    get("Получает", "#2196F3", "#ffffff"),
    give("Отдает", "#673AB7", "#ffffff"),
    look_at_field("Посмотреть поле", "#795548", "#ffffff"),
    step_end("Закончил", "#B71C1C", "#ffffff")

    //buy_third_field("Покупает поле"),


}