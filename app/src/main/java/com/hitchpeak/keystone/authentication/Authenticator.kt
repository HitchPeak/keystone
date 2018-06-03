package com.hitchpeak.keystone.authentication

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.hitchpeak.keystone.authentication.AuthenticationCommon.KEY_ADD_ACCOUNT
import com.hitchpeak.keystone.authentication.AuthenticationCommon.AUTHTOKEN_FULL_ACCESS_LABEL
import com.hitchpeak.keystone.authentication.AuthenticationCommon.KEY_ACCOUNT_TYPE
import com.hitchpeak.keystone.authentication.AuthenticationCommon.KEY_AUTH_TOKEN_TYPE
import com.hitchpeak.keystone.authentication.AuthenticationCommon.serverConnector
import android.accounts.AccountManager.KEY_BOOLEAN_RESULT


class Authenticator constructor(private val mContext: Context) : AbstractAccountAuthenticator(mContext) {

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        val am = AccountManager.get(mContext)
        var authToken = am.peekAuthToken(account, authTokenType)

        if (TextUtils.isEmpty(authToken)) {
            // The case when user was not signed in

            val password = am.getPassword(account)
            if (password != null) {
                authToken = serverConnector.userSignIn(account?.name, password, authTokenType)
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            // We got a valid token

            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)

            return result
        }

        // User wasn't able to sign in, so we need to prompt them again for credentials
        val intent = Intent(mContext, AuthenticatorActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(KEY_ACCOUNT_TYPE, account?.type)
        intent.putExtra(KEY_AUTH_TOKEN_TYPE, authTokenType)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        val result = Bundle()
        result.putBoolean(KEY_BOOLEAN_RESULT, false)
        return result
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        return AUTHTOKEN_FULL_ACCESS_LABEL
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        val intent = Intent(mContext, AuthenticatorActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(KEY_ADD_ACCOUNT, true)
        intent.putExtra(KEY_AUTH_TOKEN_TYPE, authTokenType)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? {
        // Not implemented
        return null
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? {
        // Not implemented
        return null
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle? {
        // Not implemented
        return null
    }


}