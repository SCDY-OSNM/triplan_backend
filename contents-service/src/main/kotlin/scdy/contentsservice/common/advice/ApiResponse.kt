package scdy.contentsservice.common.advice

data class ApiResponse<T>(
        val status: String = "success",
        val message: String,
        val data: T? = null
) {
    companion object {
        fun <T> success(message: String, data: T): ApiResponse<T> {
            return ApiResponse(message = message, data = data)
        }

        fun success(message: String): ApiResponse<Unit> {
            return ApiResponse(message = message)
        }

        fun <T> error(message: String, data: T? = null): ApiResponse<T> {
            return ApiResponse(status = "error", message = message, data = data)
        }
    }
}
