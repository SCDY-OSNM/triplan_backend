package scdy.contentsservice.common.exceptions

class ElasticSearchIndexException : RuntimeException {
    constructor(message:String) :super(message)
    constructor(message: String, cause:Throwable) :super(message, cause)
}