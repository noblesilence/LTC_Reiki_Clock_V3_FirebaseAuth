package com.learnteachcenter.ltcreikiclock.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
        tableName = "reikis"
)

data class Reiki (
        @Json(name = "id")
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String,

        @Json(name="seqNo")
        @ColumnInfo(name="seqNo")
        var seqNo: Int,

        @Json(name="title")
        @ColumnInfo(name = "title")
        val title: String,

        @Json(name="description")
        @ColumnInfo(name="description")
        val description: String?,

        @Json(name="playMusic")
        @ColumnInfo(name="playMusic")
        val playMusic: Boolean
) : Serializable