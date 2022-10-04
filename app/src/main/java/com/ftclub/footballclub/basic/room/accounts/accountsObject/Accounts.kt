package com.ftclub.footballclub.basic.room.accounts.accountsObject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftclub.footballclub.basic.room.accounts.AccountsDatabase

@Entity(tableName = AccountsDatabase.ACCOUNTS_TABLE_NAME)
data class Accounts (
    @ColumnInfo(name = "account_email") val accountEmail: String,
    @ColumnInfo(name = "hash_password") val hashPassword: String,
    @ColumnInfo(name = "account_role") val accountRole: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
)