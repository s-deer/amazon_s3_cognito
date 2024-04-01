//
//  DeveloperAuthenticatedIdentityProvider.m
//  cognito
//

#import <Foundation/Foundation.h>
#import "DeveloperAuthenticatedIdentityProvider.h"

@interface DeveloperAuthenticatedIdentityProvider ()
@property (strong, atomic) NSString *identityIdOverride;
@property (strong, atomic) NSDictionary *loginsOverride;
@end

@implementation DeveloperAuthenticatedIdentityProvider

- (instancetype)initWithRegionType:(AWSRegionType)regionType
                        identityId:(NSString *)identityId
                    identityPoolId:(NSString *)identityPoolId
                             token:(NSString *)token
{
    if (self = [super initWithRegionType:regionType
                          identityPoolId:identityPoolId
                         useEnhancedFlow:YES
                 identityProviderManager:nil]) {
        self.identityIdOverride = identityId;
        self.loginsOverride = @{ @"cognito-identity.amazonaws.com":token };
    }
    return self;
}

- (AWSTask *)logins {
    self.identityId = self.identityIdOverride;
    return [AWSTask taskWithResult:self.loginsOverride];
}
@end
