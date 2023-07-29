package com.yera

import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.yera.R
import com.yera.theme.WeatherTheme
import com.yera.theme.bgg
import kotlinx.coroutines.delay

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = bgg
                ) {
                    val scale = remember {
                        Animatable(0f)
                    }
                    LaunchedEffect(key1 = true) {
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = 2000,
                                easing = {
                                    OvershootInterpolator(1.3f).getInterpolation(it)
                                }
                            )
                        )
                        delay(500L)

                        val intent = Intent(this@SplashScreen, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_app),
                            contentDescription = "logo",
                            modifier = Modifier.scale(scale.value)
                        )
                    }
                }
            }
        }
    }
}

