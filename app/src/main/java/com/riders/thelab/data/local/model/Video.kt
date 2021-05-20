package com.riders.thelab.data.local.model

import android.os.Parcelable
import android.os.Parcelable.Creator
import com.squareup.moshi.Json

data class Video constructor(
    @Json(name = "id")
    val favId: String? = null,
    val videoName: String? = null,
    val videoDescription: String? = null,
    @Json(name = "imageUrl")
    val videoImageThumb: String? = null,
    val videoUrl: String? = null
) : Parcelable {

    @JvmField
    val CREATOR: Creator<Video> = object : Creator<Video> {
        override fun createFromParcel(source: android.os.Parcel): Video {
            return Video(source)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }

    var db_id: Int? = null

    @Json(name = "id")
    var fav_id: String? = null
    var name: String? = null
    var description: String? = null

    @Json(name = "imageUrl")
    var imageThumb: String? = null
    var url: String? = null


    constructor(fav_id: String, name: String) : this() {
        this.fav_id = fav_id
        this.name = name
    }

    constructor(db_id: Int, fav_id: String, name: String) : this() {
        this.db_id = db_id
        this.fav_id = fav_id
        this.name = name
    }

    /*constructor(fav_id: String?, name: String?, description: String?, imageThumb: String?, videoUrl: String?) : this() {
        this.fav_id = fav_id
        this.name = name
        this.description = description
        this.imageThumb = imageThumb
        this.videoUrl = videoUrl
    }
*/

    constructor(
        db_id: Int,
        fav_id: String,
        name: String,
        description: String,
        imageThumb: String,
        videoUrl: String
    ) : this() {
        this.db_id = db_id
        this.fav_id = fav_id
        this.name = name
        this.description = description
        this.imageThumb = imageThumb
        this.url = videoUrl
    }

    constructor(source: android.os.Parcel) : this() {
        fav_id = source.readString()
        name = source.readString()
        description = source.readString()
        imageThumb = source.readString()
        url = source.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {
        dest.writeString(fav_id)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeString(imageThumb)
        dest.writeString(videoUrl)
    }

    override fun toString(): String {
        return "Video(CREATOR=$CREATOR, db_id=$db_id, fav_id=$fav_id, name=$name, description=$description, imageThumb=$imageThumb, videoUrl=$videoUrl)"
    }
}
