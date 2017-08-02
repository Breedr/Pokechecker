package uk.breedrapps.pokechecker.model

import android.os.Parcel
import android.os.Parcelable

data class PokemonSet(val code: String,
                      val standardLegal: Boolean,
                      val expandedLegal: Boolean,
                      val releaseDate: String,
                      val series: String,
                      val name: String,
                      val totalCards: Int) : Parcelable {
    fun iconUrl() : String = BASE_IMAGE_URL + code + "_icon.png"

    fun logoUrl() : String = BASE_IMAGE_URL + code + "_logo.png"

    companion object {

        const val BASE_IMAGE_URL = "http://thedrc.co.uk/app/pokechecker/images/icons/"

        @JvmField val CREATOR: Parcelable.Creator<PokemonSet> = object : Parcelable.Creator<PokemonSet> {
            override fun createFromParcel(source: Parcel): PokemonSet = PokemonSet(source)
            override fun newArray(size: Int): Array<PokemonSet?> = arrayOfNulls(size)
        }

    }

    constructor(source: Parcel) : this(
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {dest.writeString(code)
        dest.writeInt((if(standardLegal) 1 else 0))
        dest.writeInt((if(expandedLegal) 1 else 0))
        dest.writeString(releaseDate)
        dest.writeString(series)
        dest.writeString(name)
        dest.writeInt(totalCards)
    }
}