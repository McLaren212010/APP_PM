package com.example.app_pm.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "note_table")

class Note(
    @PrimaryKey(autoGenerate = true) val id: Int? =null,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name ="priority") val prioririty: String
)