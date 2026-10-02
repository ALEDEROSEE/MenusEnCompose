package com.example.menusencompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.menusencompose.ui.CustomScaffold
import com.example.menusencompose.ui.theme.MenusEnComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenusEnComposeTheme {
                CustomScaffold()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomScaffoldPreview() {
    MenusEnComposeTheme {
        CustomScaffold()
    }
}
