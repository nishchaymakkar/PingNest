@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.chatapp.pingnest.ui.screens.settings

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.HingePolicy
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.chatapp.pingnest.R
import com.chatapp.pingnest.data.models.AppTheme
import com.chatapp.pingnest.data.models.ChatThemeType
import com.chatapp.pingnest.data.models.ChatWallpaperType
import com.chatapp.pingnest.ui.PingNestViewModel
import com.chatapp.pingnest.ui.navigation.ChatRoom
import com.chatapp.pingnest.ui.navigation.Settings
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsNavigation(
    modifier: Modifier = Modifier,
    onNavBackClicked: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val backStack = rememberNavBackStack(Settings.SettingsScreen)
    val listAndDetail = rememberListDetailSceneStrategy<NavKey>(
        backNavigationBehavior = BackNavigationBehavior.PopUntilCurrentDestinationChange,
        directive = calculatePaneScaffoldDirective(
            windowAdaptiveInfo = currentWindowAdaptiveInfo(),
            verticalHingePolicy = HingePolicy.NeverAvoid
        )
    )
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        sceneStrategy = listAndDetail,
        entryProvider = entryProvider{
            entry(Settings.SettingsScreen, metadata = ListDetailSceneStrategy.listPane(
                detailPlaceholder = {
                    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(.7f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )) {
                SettingsScreen(
                    onNavBackClicked = onNavBackClicked,
                    onAccountClicked = {
                                        val last = backStack.lastOrNull()
                                            if (last is Settings.AccountSettingsScreen) {
                                                backStack[backStack.lastIndex] = Settings.AccountSettingsScreen
                                            } else {
                                                backStack.add(Settings.AccountSettingsScreen)
                                            }
                                       },
                    onHelpClicked = {
                        val last = backStack.lastOrNull()
                        if (last is Settings.HelpSettingsScreen) {
                            backStack[backStack.lastIndex] = Settings.HelpSettingsScreen
                        } else {
                            backStack.add(Settings.HelpSettingsScreen)
                        }
                                    },
                    onAppLanguageClicked = {
                        val last = backStack.lastOrNull()
                        if (last is Settings.AppLanguageSettingsScreen) {
                            backStack[backStack.lastIndex] = Settings.AppLanguageSettingsScreen
                        } else {
                            backStack.add(Settings.AppLanguageSettingsScreen)
                        }
                        },
                    onPrivacyClicked = {
                        val last = backStack.lastOrNull()
                        if (last is Settings.PrivacySettingsScreen) {
                            backStack[backStack.lastIndex] = Settings.PrivacySettingsScreen
                        } else {
                            backStack.add(Settings.PrivacySettingsScreen)
                        }},
                    onChatThemeClicked = {
                        val last = backStack.lastOrNull()
                        if (last is Settings.ChatThemSettingsScreen) {
                            backStack[backStack.lastIndex] = Settings.ChatThemSettingsScreen
                        } else {
                        backStack.add(Settings.ChatThemSettingsScreen)
                        }
                                         },
                    onProfileClicked = {
                        val last = backStack.lastOrNull()
                        if (last is Settings.ProfileSettingsScreen) {
                            backStack[backStack.lastIndex] = Settings.ProfileSettingsScreen
                        } else {
                        backStack.add(Settings.ProfileSettingsScreen)}}
                )
            }
            entry(Settings.AccountSettingsScreen, metadata = ListDetailSceneStrategy.detailPane()){
                AccountSettings(
                    onNavBackClicked = {backStack.removeLastOrNull()},
                    onLogOutClick = viewModel::logout,
                )
            }
            entry(Settings.HelpSettingsScreen, metadata = ListDetailSceneStrategy.detailPane()){
                HelpSettings(
                    onNavBackClicked = {backStack.removeLastOrNull()}
                )
            }
            entry(Settings.PrivacySettingsScreen, metadata = ListDetailSceneStrategy.detailPane()){
                PrivacySettings(
                    onNavBackClicked = {backStack.removeLastOrNull()}
                )
            }
            entry (Settings.AppLanguageSettingsScreen, metadata = ListDetailSceneStrategy.detailPane()){
                AppLanguageSettings(
                    onNavBackClicked = {backStack.removeLastOrNull()}
                )
            }
            entry(Settings.ChatThemSettingsScreen, metadata = ListDetailSceneStrategy.detailPane()) {
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
