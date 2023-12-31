package com.example.navigationlogic

import android.net.Uri
import android.util.Log
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Feature { val route: String }

interface Command {
    fun getRoute(): String
    val args: List<NamedNavArgument>
    val feature: Feature
}

sealed class NavigationCommand(
    val subRoute: String = "main",
    private val navArgs: List<NavArg> = emptyList()
): Command {
    data class GoToMain(override val feature: Feature) : NavigationCommand()
    open class GoToDetail(override val feature: Feature) :
        NavigationCommand(DETAIL_SUBROUTE,  listOf(NavArg.CHARACTER_ID, NavArg.LOCATION_ID)) {
        open fun createRoute() = "${feature.route}/$subRoute/"
    }
    override fun getRoute() = kotlin.run { "${feature.route}/$subRoute/${linkMandatoryOptionalArgs()}" }

    //check if with empty args works fine
    private fun linkMandatoryOptionalArgs(): String = with(navArgs) {
        val (matchingArgs, nonMatchingArgs) = partition { !it.optional }
        val mandatoryArgs = matchingArgs.joinToString("/") { "{${it.key}}" }
        val optionalArgs = nonMatchingArgs.joinToString("&") { "${it.key}={${it.key}}" }
            .let { if (it.isNotEmpty()) "?$it" else "" }

        "$mandatoryArgs$optionalArgs"
    }

    override val args = navArgs.map {
        navArgument(it.key) { it.navType }
    }



//    private fun getMandatoryArguments(): String =
//        navArgs.filterNot { !it.optional }
//            .joinToString("/") { "{${it.key}}" }
//
//    private fun getOptionArguments(): String =
//        navArgs.filter { it.optional }
//            .joinToString("&") { "${it.key}={${it.key}}" }
//            .let { if (it.isNotEmpty()) "?$it" else "" }




    companion object {
        const val DETAIL_SUBROUTE = "detail"
    }
}

enum class NavArg(
    val key: String,
    val navType: NavType<*>,
    val optional: Boolean,
) {
    CHARACTER_ID("characterId", NavType.StringType, false),
    LOCATION_ID("locationId", NavType.StringType, false)
}