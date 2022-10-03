package com.ftclub.footballclub.basic.room.accountsObject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftclub.footballclub.basic.room.Database

@Entity(tableName = Database.ACCOUNTS_TABLE_NAME)
data class Accounts (
    @ColumnInfo(name = "account_email") val accountEmail: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "hash_password") val hashPassword: String)