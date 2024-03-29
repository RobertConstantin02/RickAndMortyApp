package com.example.usecase.character.implementation

import android.net.Uri
import com.example.core.Resource
import com.example.domain_model.characterDetail.CharacterPresentationScreenBO
import com.example.domain_model.characterDetail.CharacterWithLocation
import com.example.domain_repository.character.ICharacterRepository
import com.example.domain_repository.di.QCharacterRepository
import com.example.domain_repository.di.QEpisodesRepository
import com.example.domain_repository.di.QLocationRepository
import com.example.domain_repository.episode.IEpisodeRepository
import com.example.domain_repository.location.ILocationRepository
import com.example.usecase.character.IGetCharacterDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetCharacterDetailsUseCaseUseCase @Inject constructor(
    @QCharacterRepository private val characterRepository: ICharacterRepository,
    @QLocationRepository private val locationRepository: ILocationRepository,
    @QEpisodesRepository private val episodesRepository: IEpisodeRepository,
) : IGetCharacterDetailsUseCase {

    override suspend fun run(input: IGetCharacterDetailsUseCase.Params): Flow<Resource<CharacterPresentationScreenBO>> {

        return combine(
            characterRepository.getCharacter(input.characterId),
            locationRepository.getExtendedLocation(input.locationId),
        ) { characterResult, locationResult ->
            characterResult.state.combineResources(locationResult.state) { character, location ->
                CharacterWithLocation(
                    Pair(character, character?.episodes),
                    Pair(location, location?.residents)
                )
            }
        }.transform {
            val characterWithLocation = it.state.unwrap()
            combine(
                characterRepository.getCharactersByIds(getIds(characterWithLocation?.characterMainDetail?.second)),
                episodesRepository.getEpisodes(getIds(characterWithLocation?.extendedLocation?.second))
            ) { residentsResult, episodesResult ->
                emit(residentsResult.state.combineResources(episodesResult.state) { residents , episodes ->
                    CharacterPresentationScreenBO(
                        characterWithLocation?.characterMainDetail?.first,
                        characterWithLocation?.extendedLocation?.first,
                        residents,
                        episodes
                    )
                })
            }.collect()
        }
    }

    private fun getIds(urls: List<String?>?) =
        urls?.mapNotNull {
            Uri.parse(it).lastPathSegment?.toInt()
        } ?: emptyList()

}
