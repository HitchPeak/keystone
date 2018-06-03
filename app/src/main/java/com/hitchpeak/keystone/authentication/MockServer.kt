package com.hitchpeak.keystone.authentication

class MockServer {

    private val mockToken = "mockToken"

    fun userSignIn(name: String?, password: String?, authTokenType: String?): String {
        return mockToken
    }

}