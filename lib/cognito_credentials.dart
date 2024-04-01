class CognitoCredentials {
  final String identityId;
  final String identityToken;
  final String identityPoolId;

  const CognitoCredentials({
    required this.identityId,
    required this.identityToken,
    required this.identityPoolId,
  });
}
