package `fun`.aragaki.kraft.ui.base

import `fun`.aragaki.kraft.ui.theme.KraftTheme3
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AdaptSystemUIColors() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val background = MaterialTheme.colors.background

    SideEffect {
        systemUiController.apply {
            setSystemBarsColor(color = background, darkIcons = useDarkIcons)
            setNavigationBarColor(background, darkIcons = useDarkIcons)
        }
    }
}

@Composable
fun TransparentSystemUIColors(
    statusBarColor: Color = Color.Transparent,
    navBarColor: Color = Color.Transparent
) {
    val systemUiController = rememberSystemUiController()
    val darkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.apply {
            setSystemBarsColor(statusBarColor, darkIcons = darkIcons)
            setNavigationBarColor(navBarColor, darkIcons = darkIcons)
        }
    }
}
