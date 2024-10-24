package com.dicoding.eventdicoding.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "events")
@Parcelize
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val name: String?,
    val ownerName: String?,
    val description: String?,
    val quota: Int?,
    val registrans: Int?,
    val summary: String?,
    val imageLogo: String?,
    val link: String?,
    val beginTime: String?,
    var isFavorite: Boolean?
): Parcelable