package com.example.data_repository.character

import assertk.Assert
import assertk.assertThat
import assertk.assertions.support.expected
import assertk.assertions.support.show
import com.example.data_mapper.toCharacterNeighborBo
import com.example.data_repository.fake.CharacterLocalDataSourceFake
import com.example.data_repository.fake.CharacterRemoteDataSourceFake
import com.example.database.detasource.character.ICharacterLocalDatasource
import com.example.domain_model.character.CharacterNeighborBo
import com.example.remote.character.datasource.ICharacterRemoteDataSource
import com.example.test.character.CharacterUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CharacterRepositoryTest {
    private lateinit var remoteDataSource: ICharacterRemoteDataSource
    private lateinit var localDatasource: ICharacterLocalDatasource
    private lateinit var repository: CharacterRepository

    @BeforeEach
    fun setUp() {
        remoteDataSource = CharacterRemoteDataSourceFake()
        localDatasource = CharacterLocalDataSourceFake()
        repository = CharacterRepository(remoteDataSource, localDatasource)
    }

    /**
     * 1*
     * Local database does not have any data. Request to remote datasource is made.
     * @isEqualToWithGivenProperties -> only compares the fields, not class itself.
     */
    @Test
    fun `getCharactersByIds call, returns Resource Success when requesting from remote`() =
        runTest {
            val expected =
                CharacterUtil.expectedSuccessCharacters.results?.filterNotNull()?.take(3)?.map {
                    it.toCharacterNeighborBo()
                } ?: emptyList()
            //Given
            (localDatasource as CharacterLocalDataSourceFake).localError = null
            (remoteDataSource as CharacterRemoteDataSourceFake).remoteError = null
            (remoteDataSource as CharacterRemoteDataSourceFake).setCharacters(
                CharacterUtil.expectedSuccessCharacters.results?.filterNotNull() ?: listOf()
            )

            //When
            repository.getCharactersByIds(listOf(1, 2, 3)).collectLatest { result ->
                //Then
                //assertThat(result.state).isInstanceOf(Resource.State.Success::class)
                assertThat(result.state.unwrap().orEmpty()).isExpectedNeighbors(expected)
            }
        }

    /**
     * *2
     */
    @Test
    fun `getCharactersByIds call, returns Resource Success when requesting from api and local DatabaseResponseError`() = runTest {


    }

    /**
     * *3
     */
    @Test
    fun `getCharactersByIds call, returns Resource Success if DatabaseResponseEmpty`() {

    }

    /**
     * *3
     */
    @Test
    fun `getCharactersByIds call, returns Resource Success if database has not saved api data`() {

    }


    private fun Assert<List<CharacterNeighborBo>>.isExpectedNeighbors(
        expected: List<CharacterNeighborBo>
    ) = given { actual ->
        if (expected.size == actual.size && expected.zip(actual).all { (actual, expected) ->
                actual.image.value.orEmpty() == expected.image.value.orEmpty() &&
                        actual.id == expected.id
            }) return

        expected("character: ${show(expected)} but was character: ${show(actual)}")
    }
}