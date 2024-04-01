package com.famproperties.amazon_s3_cognito

import android.content.Context
import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.auth.AWSCognitoIdentityProvider
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtilityOptions
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client

class AwsHelper {
    fun getTransferUtility(
        context: Context,
        credentials: CognitoCredentials,
        region: Regions
    ): TransferUtility {
        val credentialsProvider =
            getCredentialsProvider(context.applicationContext, credentials, region)

        val s3Client =
            getS3Client(credentialsProvider, region)

        TransferNetworkLossHandler.getInstance(context.applicationContext)

        val transferOptions = TransferUtilityOptions()
        transferOptions.transferThreadPoolSize = 18

        return TransferUtility.builder()
            .s3Client(s3Client)
            .transferUtilityOptions(transferOptions)
            .context(context)
            .build();
    }

    fun getS3Client(
        credentialsProvider: CognitoCachingCredentialsProvider,
        region: Regions
    ): AmazonS3Client {

        val configuration = ClientConfiguration().apply {
            connectionTimeout = 30 * 1000
            socketTimeout = 30 * 1000
            protocol = Protocol.HTTPS
        }

        return AmazonS3Client(credentialsProvider, Region.getRegion(region), configuration)
    }


    fun getCredentialsProvider(
        context: Context,
        credentials: CognitoCredentials,
        region: Regions
    ): CognitoCachingCredentialsProvider {
        val identityProvider: AWSCognitoIdentityProvider = DeveloperAuthenticationProvider(
            credentials.identityPoolId,
            credentials.identityId,
            credentials.identityToken,
            region,
        );

        return CognitoCachingCredentialsProvider(context, identityProvider, region);
    }
}