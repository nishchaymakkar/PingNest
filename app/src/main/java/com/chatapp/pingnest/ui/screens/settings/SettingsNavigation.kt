package com.chatapp.pingnest.ui.screens.settings

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.chatapp.pingnest.data.models.AppTheme
import com.chatapp.pingnest.data.models.ChatThemeType
import com.chatapp.pingnest.data.models.ChatWallpaperType
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.navigation.Settings
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsNavigation(
    modifier: Modifier = Modifier,
    onNavBackClicked: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val backStack = rememberNavBackStack(Settings.SettingsScreen)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider{
            entry(Settings.SettingsScreen) {
                SettingsScreen(
                    onNavBackClicked = onNavBackClicked,

                    onAccountClicked = {backStack.add(Settings.AccountSettingsScreen)},
                    onHelpClicked = {backStack.add(Settings.HelpSettingsScreen)},
                    onAppLanguageClicked = {backStack.add(Settings.AppLanguageSettingsScreen)},
                    onPrivacyClicked = {backStack.add(Settings.PrivacySettingsScreen)},
                    onChatThemeClicked = {backStack.add(Settings.ChatThemSettingsScreen)},
                    onProfileClicked = {backStack.add(Settings.ProfileSettingsScreen)}
                )
            }
            entry(Settings.AccountSettingsScreen){
                AccountSettings(
                    onNavBackClicked = {backStack.removeLastOrNull()},
                    onLogOutClick = viewModel::logout,
                )
            }
            entry(Settings.HelpSettingsScreen){
                HelpSettings(
                    onNavBackClicked = {backStack.removeLastOrNull()}
                )
            }
            entry(Settings.PrivacySettingsScreen){
                PrivacySettings(
                    onNavBackClicked = {backStack.removeLastOrNull()}
                )
            }
            entry (Settings.AppLanguageSettingsScreen){
                AppLanguageSettings(
                    onNavBackClicked = {backStack.removeLastOrNull()}
                )
            }
            entry(Settings.ChatThemSettingsScreen) {
                ChatThemeSettings(
                    onNavBackClicked = {backStack.removeLastOrNull()},
                    onThemeChange = {viewModel.updateTheme(it)},
                    appTheme = viewModel.themeFlow.collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM_DEFAULT).value,
                    onChatThemeChange = {viewModel.updateChatTheme(it)},
                    defaultChatTheme = viewModel.chatThemeFlow.collectAsStateWithLifecycle(initialValue = ChatThemeType.EMERALD_GREEN).value,
                    defaultWallpaper = viewModel.wallpaperFlow.collectAsStateWithLifecycle(initialValue = ChatWallpaperType.DEFAULT).value,
                    selectedWallpaper = {viewModel.updateWallpaper(it)},
                )
            }
            entry(Settings.ProfileSettingsScreen,   metadata = NavDisplay.transitionSpec {
                // Slide new content up, keeping the old content in place underneath
                slideInHorizontally(
                    initialOffsetX= { it },
                    animationSpec = tween(1000)
                ) togetherWith ExitTransition.None
            } + NavDisplay.popTransitionSpec {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(1000)
                        )
            } + NavDisplay.predictivePopTransitionSpec {
                // Slide old content down, revealing the new content in place underneath
                EnterTransition.None togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(1000)
                        )
            } ){
                ProfileSettingsScreen(
                    onNavBackClicked = {backStack.removeLastOrNull()}
                )
            }
        },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    )
}
