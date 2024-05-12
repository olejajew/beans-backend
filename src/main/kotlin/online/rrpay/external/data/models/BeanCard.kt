package online.rrpay.external.data.models

enum class BeanCard(val countInDeck: Int, val cutLevels: List<CutLevel>) {
    ERROR(0, emptyList()),
    CacaoBean(
        4, listOf(
            CutLevel(2, 2),
            CutLevel(3, 2),
            CutLevel(4, 3)
        )
    ),
    GardenBean(
        6, listOf(
            CutLevel(2, 2),
            CutLevel(3, 4)
        )
    ),
    RedBean(
        8, listOf(
            CutLevel(2, 1),
            CutLevel(3, 2),
            CutLevel(4, 3),
            CutLevel(5, 4),
        )
    ),
    BlackEyedBean(
        10, listOf(
            CutLevel(2, 1),
            CutLevel(4, 2),
            CutLevel(5, 3),
            CutLevel(6, 4),
        )
    ),
    SoyBean(
        12, listOf(
            CutLevel(2, 1),
            CutLevel(4, 2),
            CutLevel(6, 3),
            CutLevel(7, 4),
        )
    ),
    GreenBean(
        14, listOf(
            CutLevel(3, 1),
            CutLevel(5, 2),
            CutLevel(6, 3),
            CutLevel(7, 4),
        )
    ),
    StinkBean(
        16, listOf(
            CutLevel(3, 1),
            CutLevel(5, 2),
            CutLevel(7, 3),
            CutLevel(8, 4),
        )
    ),
    ChilliBean(
        18, listOf(
            CutLevel(3, 1),
            CutLevel(6, 2),
            CutLevel(8, 3),
            CutLevel(9, 4),
        )
    ),
    BlueBean(
        20, listOf(
            CutLevel(4, 1),
            CutLevel(6, 2),
            CutLevel(8, 3),
            CutLevel(10, 4),
        )
    ),
    WaxBean(
        22, listOf(
            CutLevel(3, 1),
            CutLevel(7, 2),
            CutLevel(9, 3),
            CutLevel(11, 4),
        )
    ),
    CoffeeBean(
        24, listOf(
            CutLevel(4, 1),
            CutLevel(7, 2),
            CutLevel(10, 3),
            CutLevel(12, 4),
        )
    );

    companion object {
        fun getCutLevel(beanCard: BeanCard, count: Int): CutLevel? {
            return beanCard.cutLevels.firstOrNull { it.count == count }
        }

    }

}
