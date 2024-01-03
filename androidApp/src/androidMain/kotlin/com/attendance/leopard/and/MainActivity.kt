package com.attendance.leopard.and

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.attendace.leopard.MainView
import com.attendace.leopard.util.helper.context
import com.attendance.leopard.and.util.notificationUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this
        window.statusBarColor = ContextCompat.getColor(this, R.color.primaryColor)

        notificationUtil(this)

        setContent {
            MainView(this)
        }

    }
}