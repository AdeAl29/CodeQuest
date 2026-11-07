package com.codequest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codequest.presentation.navigation.CodeQuestApp
import com.codequest.ui.theme.CodeQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeQuestTheme {
                CodeQuestApp()
            }
        }
    }
}
