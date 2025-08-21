package com.bluesourceplus.heartspace.feature.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bluesourceplus.heartspace.R


data class OnboardingPage(
    val title: String,
    val description: String,
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to Heartspace",
        description = "Your personal space to track emotions and cultivate positivity.",
    ),
    OnboardingPage(
        title = "Track Your Mood",
        description = "Quickly log how you feel throughout the day.",
    ),
    OnboardingPage(
        title = "Reflect & Grow",
        description = "Gain insights from your mood patterns and improve wellbeing.",
    )
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var currentPage by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(250.dp)
        )

        Text(
            text = onboardingPages[currentPage].title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = onboardingPages[currentPage].description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Row {
            onboardingPages.forEachIndexed { index, _ ->
                val color = if (index == currentPage) MaterialTheme.colorScheme.primary else Color.Gray
                Box(modifier = Modifier
                    .size(16.dp)
                    .padding(4.dp)
                    .background(color, shape = CircleShape))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentPage < onboardingPages.lastIndex) {
                TextButton(onClick = { currentPage = onboardingPages.lastIndex }) {
                    Text("Skip")
                }
            }

            Button(onClick = {
                if (currentPage < onboardingPages.lastIndex) {
                    currentPage++
                } else {
                    onFinish()
                }
            }) {
                Text(if (currentPage == onboardingPages.lastIndex) "Get Started" else "Next")
            }
        }
    }
}