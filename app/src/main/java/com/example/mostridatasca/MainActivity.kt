package com.example.mostridatasca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mostridatasca.ui.theme.MostriDaTascaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MostriDaTascaTheme {
                MostriDaTasca()
            }
        }
    }
}