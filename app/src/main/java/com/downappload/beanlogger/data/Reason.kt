package com.downappload.beanlogger.data

enum class Reason(val title: String, val backgroundColor: String, val textColor: String) {
    abort("... на Абортируй по братски", "#ffc107", "#000000"),
    gift("... в дар", "#cddc39", "#000000"),
    loan("... в долг", "#2196f3", "#ffffff"),
    by_load("... за тот долг", "#4caf50", "#000000"),
    garbage("... забирай", "#ffffff", "#000000"),
    not_sure("... ХЗ зачем", "#000000", "#ffffff")
}