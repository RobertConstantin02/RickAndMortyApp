package com.example.feature_feed

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.feature_feed.list.HeroListScreen
import com.example.navigationlogic.Command


fun NavGraphBuilder.feedGraph(
    command: Command,
    nestedGraphs: NavGraphBuilder.() -> Unit,
    onItemClick: (heroId: Int, locationId: Int?) -> Unit
) {
    navigation(
        startDestination = command.getRoute(),
        route = command.feature.route
    ) {
        composable(
            route = command.getRoute(),
            arguments = command.args
        ) {
            HeroListScreen(onItemClick = onItemClick)
        }
        nestedGraphs()
    }
}

