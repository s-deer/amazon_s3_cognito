//
//  AwsHelper.swift
//  amazon_s3_cognito
//
//  Created by Alexey on 01.04.2024.
//

import Foundation
import AWSS3


class AwsHelper {
    static var awsS3CognitoIndex: Int = 0
    static func awsTranferUtilityKey(createNew: Bool = false) -> String {
        if createNew {
            awsS3CognitoIndex += 1
        }
        return "S3Transfer" + String(awsS3CognitoIndex)
    }
    
    func initTransferUtility(credentials: CognitoCredentials, region: AWSRegionType) {
        guard let identityProvider = DeveloperAuthenticatedIdentityProvider(
            regionType:region,
            identityId: credentials.identityId,
            identityPoolId: credentials.identityPoolId,
            token: credentials.identityToken
        ) else { return }
        
        let credentialsProvider = AWSCognitoCredentialsProvider(
            regionType: region,
            unauthRoleArn: nil,
            authRoleArn: nil,
            identityProvider: identityProvider
        )
        
        let configuration = AWSServiceConfiguration(region: region, credentialsProvider: credentialsProvider)
        
        let newKey = AwsHelper.awsTranferUtilityKey(createNew: true)
        
        AWSS3TransferUtility.register(with: configuration!, forKey: newKey)
    }
    
    func getTransferUtility() -> AWSS3TransferUtility? {
        return AWSS3TransferUtility.s3TransferUtility(forKey: AwsHelper.awsTranferUtilityKey())
    }
}
