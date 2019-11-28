package th.ac.up.agr.thai_mini_chicken.Data

import android.os.Parcel
import android.os.Parcelable


data class CardData(
        var dateDay: String = "",
        var dateMonth: String = "",
        var dateYear: String = "",
        var amountMale: String = "",
        var amountFemale: String = "",
        var systemFarm: String = "",
        var userObjective: String = "",
        var status: String = "",
        var createDate: String = "",
        var lastUpdate: String = "",
        var moveToHistory: String = "",
        var cardName: String = "",
        var cardID: String = "",
        var breed: String = "",
        var ageDay: String = "",
        var ageWeek: String = "",
        var notification: String = "",
        var notiBefore: String = "",
        var managerName: String = "",
        var managerObjective: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dateDay)
        parcel.writeString(dateMonth)
        parcel.writeString(dateYear)
        parcel.writeString(amountMale)
        parcel.writeString(amountFemale)
        parcel.writeString(systemFarm)
        parcel.writeString(userObjective)
        parcel.writeString(status)
        parcel.writeString(createDate)
        parcel.writeString(lastUpdate)
        parcel.writeString(moveToHistory)
        parcel.writeString(cardName)
        parcel.writeString(cardID)
        parcel.writeString(breed)
        parcel.writeString(ageDay)
        parcel.writeString(ageWeek)
        parcel.writeString(notification)
        parcel.writeString(notiBefore)
        parcel.writeString(managerName)
        parcel.writeString(managerObjective)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardData> {
        override fun createFromParcel(parcel: Parcel): CardData {
            return CardData(parcel)
        }

        override fun newArray(size: Int): Array<CardData?> {
            return arrayOfNulls(size)
        }
    }
}