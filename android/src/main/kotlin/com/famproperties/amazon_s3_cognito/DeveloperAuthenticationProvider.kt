package com.famproperties.amazon_s3_cognito

import com.amazonaws.auth.AWSAbstractCognitoIdentityProvider
import com.amazonaws.regions.Regions

class DeveloperAuthenticationProvider(
    private val identityPoolId: String,
    private val identityId: String,
    private val token: String,
    private val region: Regions
) : AWSAbstractCognitoIdentityProvider(null, identityPoolId, region) {

    init {
        this.setIdentityId(identityId)
        this.setToken(token)
    }

    override fun getProviderName(): String {
        return "cognito-identity.amazonaws.com"
    }

    override fun refresh(): String {
        this.setIdentityId(identityId)
        this.setToken(token)

        val logins: MutableMap<String, String> = HashMap()
        logins["cognito-identity.amazonaws.com"] = token
        setLogins(logins)

        this.update(identityId, token)

        return token
    }
}
