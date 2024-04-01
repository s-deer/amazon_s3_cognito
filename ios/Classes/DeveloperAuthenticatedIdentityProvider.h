//
//  DeveloperAuthenticatedIdentityProvider.h
//  cognito
//

#ifndef DeveloperAuthenticatedIdentityProvider_h
#define DeveloperAuthenticatedIdentityProvider_h

#import <AWSCore/AWSCore.h>

@interface DeveloperAuthenticatedIdentityProvider : AWSCognitoCredentialsProviderHelper

- (instancetype)initWithRegionType:(AWSRegionType)regionType
                        identityId:(NSString *)identityId
                    identityPoolId:(NSString *)identityPoolId
                             token:(NSString *)token;

- (AWSTask *)logins;
@end

#endif /* DeveloperAuthenticatedIdentityProvider_h */
