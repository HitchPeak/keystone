package com.hitchpeak.keystone.authentication

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hitchpeak.keystone.authentication.AuthenticationCommon.KEY_ADD_ACCOUNT
import com.hitchpeak.keystone.authentication.AuthenticationCommon.AUTHTOKEN_FULL_ACCESS_LABEL
import com.hitchpeak.keystone.authentication.AuthenticationCommon.KEY_AUTH_TOKEN_TYPE

class Authenticator constructor(private val mContext: Context) : AbstractAccountAuthenticator(mContext) {

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}