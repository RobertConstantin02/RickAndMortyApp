package com.example.core.remote

/**
 * The factory methods in the companion object (loading, success, emptySuccess, error) provide a
 * convenient and consistent way to create instances of the Resource class. This can improve
 * readability and reduce boilerplate code when creating instances with specific states.
 */
class Resource<out T> private constructor(
    val state: State<T>
) {
    sealed class State<out T> {

        data class Success<out T>(val data: T) : State<T>()
        data class SuccessEmpty<out T>(val data: T?) : State<T>()

        data class Error<out T>(val apiError: String?, val localError: Int?, val data: T?) :
            State<T>()

        //        fun <R1, R2, W> Resource.State<R1>.combineResource(
//            resource: Resource.State<R2>,
//            transform: (a: R1, b: R2) -> W,
//            //currentState: State<T>,
//            //unSuccess: (currentState : State<T>) -> Resource()
//        ): Resource<W> =
//            when {
//                this is Resource.State.Success && resource is State.Success -> Resource(
//                    State.Success(transform(this.data, resource.data))
//                )
//                this is Resource.State.Error -> Resource(State.Error(this.apiError, this.localError, this.data))
//                else -> Resource(Loading)
//            }

        fun <R, U> combineResources(
            other: State<R>,
            combineFunction: (T?, R?) -> U
        ): Resource<U> = when (this) {
            is Success -> when (other) {
                is Success -> Resource(Success(combineFunction(data, other.data)))
                is SuccessEmpty -> Resource(Success(combineFunction(data, other.data)))
                is Error -> Resource(Error(other.apiError, other.localError, combineFunction(data, other.data)))
            }
            is SuccessEmpty -> when (other) {
                is Success -> Resource(Success(combineFunction(data, other.data)))
                is SuccessEmpty -> Resource(Success(combineFunction(data, other.data)))
                is Error -> Resource(Error(other.apiError, other.localError, combineFunction(data, other.data)))
            }
            is Error -> Resource(Error(apiError, localError, combineFunction(data, (other as? Error)?.data)))
        }

        fun unwrap(): T? {
            return when (this) {
                is State.Success -> this.data
                is State.SuccessEmpty -> this.data
                is State.Error -> this.data
            }
        }
//        fun <R> fold(
//            success: (T) -> R,
//            error: (String?, Int?, T?) -> Error<R>,
//            empty: (T?) -> R
//        ): Resource<R> =
//            when (this) {
//                is Success -> Resource(State.Success(success(this.data)))
//                is SuccessEmpty -> Resource(State.SuccessEmpty(empty(this.data)))
//                is Error -> Resource(error(this.apiError, this.localError, this.data))
//            }
//
//
//        fun <R1, R2, W> Resource.State<R1>.combineSuccess(
//            //maybe if we dont get a success we can emit the current state. Lets play with flows and collect to emit what we want.
//            resource: Resource.State<R2>,
//            transform: (a: R1, b: R2) -> W,
//        ): Resource<W>? = let {
//            if (this is Resource.State.Success && resource is State.Success)
//                Resource(State.Success(transform(this.data, resource.data)))
//            else null
//        }
//
//        fun <R> unWrap(
//            success: (T) -> R,
//            noSuccess: (State<T>) -> State<T>
//        ): R? =
//            if (this is Success)
//                success(this.data)
//            else null


//        fun <T, R> Resource<T>.mapData(transform: (T?) -> R): Resource<R> {
//            return when (val currentState = state) {
//                is Resource.State.Loading -> Resource(transform())
//                is Resource.State.Success -> Resource(Resource.State.Success(transform(currentState.data)))
//                is Resource.State.SuccessEmpty -> Resource(
//                    Resource.State.SuccessEmpty(
//                        transform(
//                            currentState.data
//                        )
//                    )
//                )
//
//                is Resource.State.Error -> Resource(
//                    Resource.State.Error(
//                        currentState.apiError,
//                        currentState.localError,
//                        transform(currentState.data)
//                    )
//                )
//            }
//        }

    }


    companion object {
        //fun loading() = Resource(State.Loading)

        fun <T> success(data: T) = Resource(State.Success(data))

        fun <T> successEmpty(data: T?) = Resource(State.SuccessEmpty(data)) //review at the end

        fun <T> error(
            errorMessage: String,
            data: T?
        ) = Resource(State.Error(errorMessage, null, data))

        fun <T> error(
            errorResource: Int,
            data: T?
        ) = Resource(State.Error(null, errorResource, data))
    }
}