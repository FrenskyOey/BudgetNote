package com.app.budgetnote

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform