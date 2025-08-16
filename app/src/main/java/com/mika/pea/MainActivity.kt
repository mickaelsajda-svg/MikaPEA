package com.mika.pea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.mika.pea.ui.PortfolioApp

class MainActivity : ComponentActivity() {
    private val vm: PortfolioViewModel by viewModels {
        PortfolioViewModel.Factory((application as MikaPeaApp).container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    PortfolioApp(vm = vm)
                }
            }
        }
    }
}
