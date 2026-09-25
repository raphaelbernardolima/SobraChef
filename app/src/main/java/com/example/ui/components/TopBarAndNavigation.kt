package com.example.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SobraChefTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = PrimaryGreen
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MintContainer,
            titleContentColor = PrimaryGreen,
            navigationIconContentColor = PrimaryGreen,
            actionIconContentColor = PrimaryGreen
        )
    )
}

@Composable
fun SobraChefBottomNav(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    shoppingCount: Int = 0,
    savedCount: Int = 0
) {
    NavigationBar(
        containerColor = MintContainer,
        contentColor = PrimaryGreen
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onNavigate(AppScreen.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Kitchen else Icons.Outlined.Kitchen,
                    contentDescription = "Início e Despensa"
                )
            },
            label = {
                Text(
                    text = "Despensa",
                    fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Medium
                )
            },
            modifier = Modifier.testTag("nav_home"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                unselectedIconColor = TextSecondaryLight,
                selectedTextColor = PrimaryGreen,
                unselectedTextColor = TextSecondaryLight,
                indicatorColor = PrimaryGreenLight.copy(alpha = 0.2f)
            )
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.RECIPES,
            onClick = { onNavigate(AppScreen.RECIPES) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.RECIPES) Icons.AutoMirrored.Filled.MenuBook else Icons.AutoMirrored.Outlined.MenuBook,
                    contentDescription = "Receitas"
                )
            },
            label = {
                Text(
                    text = "Receitas",
                    fontWeight = if (currentScreen == AppScreen.RECIPES) FontWeight.Bold else FontWeight.Medium
                )
            },
            modifier = Modifier.testTag("nav_recipes"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                unselectedIconColor = TextSecondaryLight,
                selectedTextColor = PrimaryGreen,
                unselectedTextColor = TextSecondaryLight,
                indicatorColor = PrimaryGreenLight.copy(alpha = 0.2f)
            )
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.MEAL_PLAN,
            onClick = { onNavigate(AppScreen.MEAL_PLAN) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.MEAL_PLAN) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                    contentDescription = "Cardápio Semanal"
                )
            },
            label = {
                Text(
                    text = "Cardápio",
                    fontWeight = if (currentScreen == AppScreen.MEAL_PLAN) FontWeight.Bold else FontWeight.Medium
                )
            },
            modifier = Modifier.testTag("nav_meal_plan"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                unselectedIconColor = TextSecondaryLight,
                selectedTextColor = PrimaryGreen,
                unselectedTextColor = TextSecondaryLight,
                indicatorColor = PrimaryGreenLight.copy(alpha = 0.2f)
            )
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.SHOPPING,
            onClick = { onNavigate(AppScreen.SHOPPING) },
            icon = {
                if (shoppingCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Terracotta,
                                contentColor = Color.White
                            ) {
                                Text(shoppingCount.toString(), fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.SHOPPING) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                            contentDescription = "Lista de Compras"
                        )
                    }
                } else {
                    Icon(
                        imageVector = if (currentScreen == AppScreen.SHOPPING) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                        contentDescription = "Lista de Compras"
                    )
                }
            },
            label = {
                Text(
                    text = "Compras",
                    fontWeight = if (currentScreen == AppScreen.SHOPPING) FontWeight.Bold else FontWeight.Medium
                )
            },
            modifier = Modifier.testTag("nav_shopping"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                unselectedIconColor = TextSecondaryLight,
                selectedTextColor = PrimaryGreen,
                unselectedTextColor = TextSecondaryLight,
                indicatorColor = PrimaryGreenLight.copy(alpha = 0.2f)
            )
        )
    }
}
