package com.bluesourceplus.heartspace

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.bluesourceplus.heartspace.feature.aboutmoodentry.AboutMoodRoute
import com.bluesourceplus.heartspace.feature.create.CreateMoodMode
import com.bluesourceplus.heartspace.feature.create.CreateScreenRoute
import com.bluesourceplus.heartspace.feature.focus.FocusScreenRoute
import com.bluesourceplus.heartspace.feature.home.HomeScreenRoute
import com.bluesourceplus.heartspace.feature.mood.MoodScreenRoute
import com.bluesourceplus.heartspace.feature.onboard.OnboardingScreen
import com.bluesourceplus.heartspace.feature.reflect.ReflectScreenRoute
import com.yourapp.heartspace.ui.settings.PreferencesScreen

@Composable
fun MeentScreenHost(
    navController: NavHostController,
    padding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = ONBOARDING_SCREEN_ROUTE,
        modifier =
        Modifier
            .padding(padding)
            .fillMaxSize(),
    ) {

        appScreen(Destination.Onboarding) {
            OnboardingScreen {
                navController.navigate(Destination.Home.route) { popUpTo(ONBOARDING_SCREEN_ROUTE) { inclusive = true } }
            }
        }

        appScreen(Destination.Preferences) {
            PreferencesScreen(back = navController::popBackStack)
        }


        appScreen(Destination.Reflect) {
            ReflectScreenRoute()
        }

        appScreen(Destination.Mood) {
            MoodScreenRoute()
        }

        appScreen(Destination.Home) {
            HomeScreenRoute(onAddButton = {
                navController.navigate(JOURNAL_SCREEN_ROUTE)
            }, onMoodCardPressed = {
                navController.navigate("$ABOUT_MOOD_SCREEN_ROUTE/$it") { launchSingleTop = true }
            }, onPrefsPressed = { navController.navigate(PREFERENCES_SCREEN_ROUTE) }
            )
        }

        appScreen(Destination.AboutMood) { backStackEntry ->
            backStackEntry.arguments?.getInt(MOOD_ID_ARG)?.let { moodId ->
                AboutMoodRoute(
                    moodId = moodId,
                    back = navController::popBackStack,
                    onUpdateMoodPressed = {
                        navController.navigate("$JOURNAL_SCREEN_ROUTE?$MOOD_ID_ARG=$moodId")
                    }
                )
            }
        }

        appScreen(Destination.Focus) {
            FocusScreenRoute()
        }

        appScreen(Destination.Journal) { backStackEntry ->
            val moodId = backStackEntry.arguments?.getString(MOOD_ID_ARG)
            val mode = if (moodId != null) {
                CreateMoodMode.Edit(Integer.parseInt(moodId))
            } else {
                CreateMoodMode.Create
            }
            CreateScreenRoute(mode = mode, back = navController::popBackStack)
        }
    }
}

@Composable
internal fun BottomBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomBarItem(
            rootScreen = Destination.Home,
            currentDestination = currentDestination,
            navController = navController,
            usualIcon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home") },
            selectedIcon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home Selected") },
            title = "Home",
        )

        BottomBarItem(
            rootScreen = Destination.Mood,
            currentDestination = currentDestination,
            navController = navController,
            usualIcon = { Icon(imageVector = Icons.Outlined.Mood, contentDescription = "Mood") },
            selectedIcon = { Icon(imageVector = Icons.Filled.Mood, contentDescription = "Mood Selected") },
            title = "Mood",
        )

        BottomBarItem(
            rootScreen = Destination.Focus,
            currentDestination = currentDestination,
            navController = navController,
            usualIcon = { Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = "Focus") },
            selectedIcon = { Icon(imageVector = Icons.Filled.AccessTime, contentDescription = "Focus Selected") },
            title = "Focus",
        )

        BottomBarItem(
            rootScreen = Destination.Reflect,
            currentDestination = currentDestination,
            navController = navController,
            usualIcon = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Reflect") },
            selectedIcon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Reflect Selected") },
            title = "Reflect",
        )

    }
}

@Composable
private fun RowScope.BottomBarItem(
    rootScreen: Screen,
    currentDestination: NavDestination?,
    navController: NavController,
    usualIcon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = usualIcon,
    title: String,
) {
    val selected = currentDestination?.hierarchy?.any { it.route == rootScreen.route } == true
    NavigationBarItem(
        selected = selected,
        onClick = {
            navController.navigate(rootScreen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = if (selected) selectedIcon else usualIcon,
        alwaysShowLabel = false,
        label = {
            AnimatedVisibility(
                visible = selected,
                enter = expandVertically(
                    expandFrom = Alignment.Bottom,
                    animationSpec = spring()
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = spring()
                )
            ) {
                Text(text = title, fontSize = 12.sp)
            }
        },
    )
}

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
)

fun NavGraphBuilder.appScreen(
    screen: Screen,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = screen.route,
        arguments = screen.arguments,
        content = content,
    )
}

const val ONBOARDING_SCREEN_ROUTE = "Onboarding"
const val HOME_SCREEN_ROUTE = "Home"
const val JOURNAL_SCREEN_ROUTE = "Journal"
const val MOOD_SCREEN_ROUTE = "Mood"
const val REFLECT_SCREEN_ROUTE = "Reflect"
const val FOCUS_SCREEN_ROUTE = "Focus"
const val PREFERENCES_SCREEN_ROUTE = "Preferences"
const val ABOUT_MOOD_SCREEN_ROUTE = "About_mood"
const val MOOD_ID_ARG = "Mood_Id"

object Destination {

    data object Home : Screen(
        route = HOME_SCREEN_ROUTE,
    )

    data object Journal : Screen(
        route = JOURNAL_SCREEN_ROUTE,
    )

    data object Mood : Screen(
        route = MOOD_SCREEN_ROUTE,
    )

    data object Focus : Screen(
        route = FOCUS_SCREEN_ROUTE,
    )

    data object Reflect : Screen (
        route = REFLECT_SCREEN_ROUTE,
    )

    data object Onboarding : Screen(
        route = ONBOARDING_SCREEN_ROUTE,
    )

    data object Preferences : Screen(
        route = PREFERENCES_SCREEN_ROUTE,
    )

    data object AboutMood : Screen(
        route = "$ABOUT_MOOD_SCREEN_ROUTE/{$MOOD_ID_ARG}",
        arguments =
        listOf(
            navArgument(MOOD_ID_ARG) {
                type = NavType.IntType
                nullable = false
            },
        ),
    )
}