package com.example.database.dao.character

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entities.CharacterEntity

@Dao
interface ICharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(vararg characters: CharacterEntity): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity): Long

    @Query("SELECT * FROM character_entity")
    fun getAllCharacters(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character_entity WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity?


    @Query("SELECT * FROM character_entity WHERE id IN (:charactersIds)")
    suspend fun getCharactersByIds(charactersIds: List<Int>): List<CharacterEntity>?

    @Query("UPDATE character_entity SET is_Favorite= :isFavorite WHERE id = :characterId")
    suspend fun updateCharacterIsFavorite(isFavorite: Boolean, characterId: Int) //put int

}