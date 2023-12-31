package com.example.domain_repository.location

import com.example.domain_model.location.ExtendedLocationBo
import com.example.resources.Result
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getExtendedLocation(extendedLocationId: Int): Flow<Result<ExtendedLocationBo>>
}