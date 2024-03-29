package com.example.core.local

import com.example.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

suspend inline fun <LOCAL> localResource(
    crossinline fetchFromLocal: suspend () -> Flow<DatabaseResponse<LOCAL>>
): Resource<LOCAL> {

    return when (val localResponse = fetchFromLocal().first()) {
        is DatabaseResponseSuccess -> Resource.success(data = localResponse.data)
        is DatabaseResponseError -> {
            Resource.error(localResponse.databaseUnifiedError.messageResource, null)
        }

        is DatabaseResponseEmpty -> Resource.successEmpty()
    }
}

inline fun <DB, BO> localResourceFlow(
    crossinline fetchFromLocal: () -> Flow<DatabaseResponse<DB>>,
    crossinline mapLocalToDomain: (DB) -> BO,
) = channelFlow<Resource<BO>> {
    fetchFromLocal().collectLatest { localResponse ->
        when (localResponse) {
            is DatabaseResponseSuccess -> {
                send(Resource.success(data = mapLocalToDomain(localResponse.data)))
            }

            is DatabaseResponseError -> {
                send(Resource.error(localResponse.databaseUnifiedError.messageResource, null))
            }

            is DatabaseResponseEmpty -> send(Resource.successEmpty())
        }
    }
}