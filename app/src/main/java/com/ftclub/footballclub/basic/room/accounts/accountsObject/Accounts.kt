package com.ftclub.footballclub.basic.room.accounts.accountsObject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase

@Entity(tableName = AccountsDatabase.ACCOUNTS_TABLE_NAME)
data class Accounts(
    @ColumnInfo(name = "account_email") val accountEmail: String,
    @ColumnInfo(name = "hash_password") val hashPassword: String,
    @ColumnInfo(name = "account_role") val accountRole: Boolean,
    @ColumnInfo(name = "date_time") val dateTime: String,
    @ColumnInfo(name = "player_information") val playerInformation: String,
    @ColumnInfo(name = "player_position") val playerPosition: String,
    @ColumnInfo(name = "player_age") val playerAge: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
): Serializable