package au.com.lexicon.herecomesthesun.service.model

sealed class ServiceResult<TSuccess, TError> {
    data class Success<TSuccess, TError>(val data: TSuccess) : ServiceResult<TSuccess, TError>()
    data class Error<TSuccess, TError>(val error: TError) : ServiceResult<TSuccess, TError>()
}
