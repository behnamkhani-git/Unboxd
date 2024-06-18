package com.behnamkhani.unboxd.common.domain

import java.io.IOException


class NoMoreProductsException(message: String): Exception(message)

class NetworkUnavailableException(message: String = "Please check your internet connection!") : IOException(message)

class NetworkException(message: String): Exception(message)