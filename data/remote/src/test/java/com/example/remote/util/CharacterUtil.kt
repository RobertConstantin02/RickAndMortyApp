package com.example.remote.util

import com.example.api.model.character.CharacterDto
import com.example.api.model.character.FeedCharacterDto
import com.example.api.model.episode.EpisodeDto
import com.example.api.model.location.ExtendedLocationDto
import com.example.remote.extension.GsonAdapterExt.fromJson

const val ALL_CHARACTERS_JSON = "json/getAllCharacters.json"
const val EMPTY_JSON = "json/empty.json"
const val RESULTS_NULL_JSON = "json/getAllCharactersWithResultsNull.json"
const val RESULTS_EMPTY_JSON = "json/getAllCharactersWithResultsEmpty.json"
const val RESULTS_EMPTY_EPISODES_JSON = "json/getAllCharactersWithEmptyEpisodes.json"
//Episodes
const val EPISODES_BY_ID_JSON = "json/getEpisodesByIds.json"
//Location
const val EXTENDED_LOCATION = "json/getLocation.json"
object CharacterUtil {
    //character
    val expectedSuccessCharacters = FileUtil.getJson(ALL_CHARACTERS_JSON)?.fromJson<FeedCharacterDto>() as FeedCharacterDto
    val expectedEmptyCharacterResponse = FileUtil.getJson(EMPTY_JSON)?.fromJson<CharacterDto>() as CharacterDto
    val expectedResultsNullResponse = FileUtil.getJson(RESULTS_NULL_JSON)?.fromJson<FeedCharacterDto>() as FeedCharacterDto
    val expectedResultsEmptyResponse = FileUtil.getJson(RESULTS_EMPTY_EPISODES_JSON)?.fromJson<FeedCharacterDto>() as FeedCharacterDto
    //episode
    val expectedSuccessEpisodesByIds = FileUtil.getJson(EPISODES_BY_ID_JSON)?.fromJson<List<EpisodeDto>>() as List<EpisodeDto>
    //location
    val expectedSuccessLocation = FileUtil.getJson(EXTENDED_LOCATION)?.fromJson<ExtendedLocationDto>() as ExtendedLocationDto
}