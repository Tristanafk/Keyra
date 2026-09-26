package com.example.keyra.data

import android.content.Context

object SessionStore {
    private const val PREF = "keyra_session"
    private const val KEY_EMAIL = "last_email"
    private const val KEY_BIOMETRIC = "biometric_enabled"

    fun saveEmail(ctx: Context, email: String) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putString(KEY_EMAIL, email).apply()
    }
    fun loadEmail(ctx: Context): String? =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_EMAIL, null)

    fun clearEmail(ctx: Context) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().remove(KEY_EMAIL).apply()
    }
    fun setBiometric(ctx: Context, enabled: Boolean) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putBoolean(KEY_BIOMETRIC, enabled).apply()
    }
    fun isBiometric(ctx: Context): Boolean =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).getBoolean(KEY_BIOMETRIC, false)
}
