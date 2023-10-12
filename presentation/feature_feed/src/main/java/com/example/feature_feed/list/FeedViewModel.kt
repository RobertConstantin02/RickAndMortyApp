package com.example.feature_feed.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.presentation_mapper.toCharacterVo
import com.example.presentation_model.CharacterVo
import com.example.usecase.character.IGetAllCharactersUseCase
import com.example.usecase.character.IUpdateCharacterIsFavoriteUseCase
import com.example.usecase.character.UpdateParams
import com.example.usecase.di.GetCharacters
import com.example.usecase.di.UpdateCharacterIsFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    @GetCharacters private val getAllCharacters: IGetAllCharactersUseCase,
    @UpdateCharacterIsFavorite private val updateCharacterIsFavorite: IUpdateCharacterIsFavoriteUseCase
) : ViewModel() {

    val feedState: Flow<PagingData<CharacterVo>> =
        getAllCharacters.invoke().map {
            it.map { character -> character.toCharacterVo() }
        }.cachedIn(viewModelScope)

    fun updateCharacter(isFavorite: Boolean, characterId: Int) {
        updateCharacterIsFavorite.invoke(
            UpdateParams(isFavorite, characterId),
            viewModelScope,
            Dispatchers.IO
        )
    }
}