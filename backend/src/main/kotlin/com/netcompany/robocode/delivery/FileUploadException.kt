package com.netcompany.robocode.delivery

class FileUploadException(private val msg: String) : RuntimeException(msg) {
    override val message: String
        get() = msg
}