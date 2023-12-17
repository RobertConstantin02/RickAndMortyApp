package com.example.data_repository.episode

import com.example.core.apiDbBoundResource
import com.example.core.remote.Resource
import com.example.data_mapper.DtoToEpisodeBo.toEpisodesBo
import com.example.data_mapper.DtoToEpisodeEntityMapper.toEpisodesEntities
import com.example.data_mapper.EntityToEpisodeBoMapper.toEpisodesBo
import com.example.data_repository.character.DAY_IN_MILLIS
import com.example.database.detasource.episode.IEpisodeLocalDataSource
import com.example.domain_model.episode.EpisodeBo
import com.example.domain_repository.episode.IEpisodeRepository
import com.example.preferences.datasource.ISharedPreferenceDataSource
import com.example.remote.episode.datasource.IEpisodeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val remote: IEpisodeRemoteDataSource,
    private val local: IEpisodeLocalDataSource,
    private val sharedPreference: ISharedPreferenceDataSource
) : IEpisodeRepository {
    override fun getEpisodes(episodesIds: List<Int>): Flow<Resource<List<EpisodeBo>>> =
        apiDbBoundResource(
            fetchFromLocal = { local.getEpisodes(episodesIds) },
            shouldMakeNetworkRequest = { databaseResult ->
                System.currentTimeMillis() - sharedPreference.getTime() >= DAY_IN_MILLIS
            },
            makeNetworkRequest = { remote.getEpisodesByIds(episodesIds) },
            saveApiData = { episodesDto ->
                local.insertEpisodes(episodesDto?.filterNotNull()?.toEpisodesEntities().orEmpty())
            },
            mapApiToDomain = { episodesDto ->
                episodesDto?.filterNotNull()?.toEpisodesBo().orEmpty()
            },
            mapLocalToDomain = { episodesEntity ->
                episodesEntity.toEpisodesBo()
            }
        )
}