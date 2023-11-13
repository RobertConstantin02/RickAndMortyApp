package com.example.usecase.character

import com.example.domain_model.character.CharacterBo
import com.example.usecase.FlowUseCase

interface IGetFavoriteCharactersUseCase : FlowUseCase<FavoritesParams, List<CharacterBo>>

data class FavoritesParams(val page: Int, val offset: Int = OFFSET)

const val OFFSET = 10