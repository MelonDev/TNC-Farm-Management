package th.ac.up.agr.thai_mini_chicken.Data

data class CardSlot(
        var dataCard: CardData = CardData(),
        var event: Event = Event(),
        var day: String = "",
        var duration: Int = 0
)