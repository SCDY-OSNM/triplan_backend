package scdy.boardservice.exception

import scdy.boardservice.common.exceptions.InternalServerException


class ElasticsearchIndexException(message: String) : InternalServerException(message)  {
}