package th.ac.up.agr.thai_mini_chicken.CallBack

import th.ac.up.agr.thai_mini_chicken.Data.CardSlot

interface ProgramCardCallBack {
    fun onCallback(value: List<CardSlot>?)
}