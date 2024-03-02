package com.example.usecase.character.fake

import androidx.paging.PagingData
import com.example.domain_model.character.CharacterBo
import com.example.domain_model.character.CharacterNeighborBo
import com.example.domain_model.characterDetail.CharacterDetailBo
import com.example.domain_model.error.DomainApiUnifiedError
import com.example.domain_model.error.DomainUnifiedError
import com.example.domain_model.resource.DomainResource
import com.example.domain_repository.character.ICharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

private const val TEST_ERROR_MESSAGE  = "Error Test"
class CharacterRepositoryFake: ICharacterRepository {

    private val charactersBo = MutableStateFlow<List<CharacterBo>>(emptyList())
    private val charactersDetailBo = MutableStateFlow<List<CharacterDetailBo>?>(emptyList())
    private val charactersNeighborBo = MutableStateFlow<List<CharacterNeighborBo>?>(emptyList())

    var error: DomainUnifiedError? = null
    var empty: DomainResource.DomainState.SuccessEmpty? = null

    fun setCharactersDetailBo(characters: List<CharacterDetailBo>?) {
        this.charactersDetailBo.value = characters
    }

    fun setCharactersNeighborBo(characters: List<CharacterNeighborBo>?) {
        this.charactersNeighborBo.value = characters
    }


    override fun getAllCharacters(): Flow<PagingData<CharacterBo>> =
        flowOf(PagingData.from(charactersBo.value))

    override fun getCharacter(characterId: Int): Flow<DomainResource<CharacterDetailBo>> {
        if (error != null) return flowOf(DomainResource.error(error!!, charactersDetailBo.value?.firstOrNull { character -> character.id == characterId }))
        if (empty != null) return flowOf(DomainResource.successEmpty())

        return charactersDetailBo.map { characters ->
            characters?.singleOrNull { character ->
                character.id == characterId
            }?.let { character ->
                DomainResource.success(character)
            } ?: DomainResource.error(DomainApiUnifiedError.Generic(TEST_ERROR_MESSAGE),  null)
        }
    }

    override fun getCharactersByIds(charactersIds: List<Int>): Flow<DomainResource<List<CharacterNeighborBo>>> {
        if (error != null) return flowOf(DomainResource.error(error!!, charactersNeighborBo.value?.filter { character -> character.id in charactersIds }))
        if (empty != null) return flowOf(DomainResource.successEmpty())

        return charactersNeighborBo.map { characters->
            characters?.filter { character ->
                character.id in charactersIds
            }?.let { charactersByIds ->
                if (charactersByIds.isEmpty())
                    DomainResource.error(DomainApiUnifiedError.Generic(TEST_ERROR_MESSAGE),  null)
                else DomainResource.success(charactersByIds)
            } ?: DomainResource.successEmpty()
        }
    }

    override suspend fun updateCharacterIsFavorite(
        isFavorite: Boolean,
        characterId: Int
    ): Flow<DomainResource<Unit>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteCharacters(
        page: Int,
        offset: Int
    ): Flow<DomainResource<List<CharacterBo>>> {
        TODO("Not yet implemented")
    }
}