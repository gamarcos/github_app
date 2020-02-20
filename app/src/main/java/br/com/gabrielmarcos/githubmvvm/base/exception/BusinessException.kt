package br.com.gabrielmarcos.githubmvvm.base.exception

import br.com.gabrielmarcos.githubmvvm.base.exception.BusinessException.Type.*

class BusinessException : RuntimeException {

    var errorType: Enum<*>? = null
        private set
    var httpCode = 0
        private set

    constructor(type: Enum<*>?) {
        errorType = type
    }

    constructor(message: String?, errorType: Enum<*>?) : super(message) {
        this.errorType = errorType
    }

    constructor(
        message: String?,
        errorType: Enum<*>?,
        httpCode: Int
    ) : super(message) {
        this.errorType = errorType
        this.httpCode = httpCode
    }

    constructor(
        message: String?,
        cause: Throwable?,
        errorType: Enum<*>?
    ) : super(message, cause) {
        this.errorType = errorType
    }

    constructor(cause: Throwable?, errorType: Enum<*>?) : super(cause) {
        this.errorType = errorType
    }

    constructor() : this(GENERIC_ERROR) {}
    constructor(cause: Throwable?) : this(cause, GENERIC_ERROR) {}
    constructor(message: String?) : this(message, GENERIC_ERROR) {}
    constructor(message: String?, cause: Throwable?) : this(
        message,
        cause,
        GENERIC_ERROR)

    enum class Type {
        GENERIC_ERROR
    }

}