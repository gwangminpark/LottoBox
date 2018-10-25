package com.prangbi.android.lottobox.helper

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.ActionBar
import android.widget.ProgressBar
import android.widget.Toast
import com.prangbi.android.lottobox.R
import com.prangbi.android.lottobox.controller.WebActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Prangbi on 9/8/16.
 */
class Util: BroadcastReceiver() {
    // Definition
    enum class NetworkType {
        None,
        Wifi,
        Mobile
    }

    // Companion
    companion object {
        private var toast: Toast? = null

        // String Function
        fun amountString(amount: Any): String {
            val format = DecimalFormat("#,###")
            return format.format(amount)
        }

        fun localizedAmountString(amount: Any): String {
            val locale = Locale("ko", "KR")
            val format = NumberFormat.getCurrencyInstance(locale)
            return format.format(amount)
        }

        // Date Function
        fun latestDrawNumber(startDateString: String, dateFormat: String): Int {
            var latestNumber = 0
            val startTime = SimpleDateFormat(dateFormat).parse(startDateString).time
            val nowTime = Date().time
            var diffTime = nowTime - startTime
            if (0L > diffTime) {
                diffTime *= -1
            }
            if (0L != diffTime) {
                latestNumber = (diffTime / (24*60*60*1000) / 7).toInt()
            }
            latestNumber += 1
            return latestNumber
        }

        // UI Function
        fun showToast(context: Context, message: String) {
            toast?.let {
                toast?.cancel()
                toast = null
            }

            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast?.show()
        }

        fun makeLoadingDialog(context: Context): Dialog {
            val dialog = Dialog(context, R.style.ProgressDialog)
            dialog.addContentView(
                    ProgressBar(context),
                    ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
            )
            return dialog
        }

        fun getNLottoNumberBackground(context: Context, number: Int): Drawable? {
            var drawable: Drawable? = null
            when (number) {
                in 1..10 -> drawable = ContextCompat.getDrawable(context, R.drawable.nlotto_num1)
                in 11..20 -> drawable = ContextCompat.getDrawable(context, R.drawable.nlotto_num11)
                in 21..30 -> drawable = ContextCompat.getDrawable(context, R.drawable.nlotto_num21)
                in 31..40 -> drawable = ContextCompat.getDrawable(context, R.drawable.nlotto_num31)
                in 41..45 -> drawable = ContextCompat.getDrawable(context, R.drawable.nlotto_num41)
            }
            return drawable
        }

        fun getPLottoGroupBackground(context: Context, number: Int): Drawable? {
            var drawable: Drawable? = null
            when (number) {
                1 -> drawable = ContextCompat.getDrawable(context, R.drawable.plotto_group1)
                2 -> drawable = ContextCompat.getDrawable(context, R.drawable.plotto_group2)
                3 -> drawable = ContextCompat.getDrawable(context, R.drawable.plotto_group3)
                4 -> drawable = ContextCompat.getDrawable(context, R.drawable.plotto_group4)
                5 -> drawable = ContextCompat.getDrawable(context, R.drawable.plotto_group5)
                6 -> drawable = ContextCompat.getDrawable(context, R.drawable.plotto_group6)
                7 -> drawable = ContextCompat.getDrawable(context, R.drawable.plotto_group7)
            }
            return drawable
        }

        fun moveToWebActivity(context: Context, title: String, url: String) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }

        // Network Function
        fun checkInternetPermission(context: Context): Boolean {
            val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
            return PermissionChecker.PERMISSION_GRANTED == permissionCheck
        }

        fun getActiveNetworkType(context: Context): Util.NetworkType {
            var networkType = Util.NetworkType.None
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            when (connectivityManager.activeNetworkInfo?.type) {
                ConnectivityManager.TYPE_WIFI -> networkType = Util.NetworkType.Wifi
                ConnectivityManager.TYPE_MOBILE -> networkType = Util.NetworkType.Mobile
            }
            return networkType
        }

        fun canConnectToInternet(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false
        }
    }

    // Broadcast Receiver
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if ("android.net.conn.CONNECTIVITY_CHANGE" == action) {
            val connChangedIntent = Intent("com.prangbi.android.lottopop.CONNECTIVITY_CHANGE")
            context.sendBroadcast(connChangedIntent)
        }
    }
}
