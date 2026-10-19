package com.example.wear

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform