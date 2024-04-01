import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

import 'aws_region.dart';
import 'cognito_credentials.dart';


class AmazonS3Cognito {
  static const MethodChannel _channel = const MethodChannel('amazon_s3_cognito');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String?> upload({
    required CognitoCredentials credentials,
    required File file,
    required String bucket,
    required String key,
    required String region,
  }) async {
    final params = {
      'filePath': file.path,
      'bucket': bucket,
      'key': key,
      'region': region,
      'identityId': credentials.identityId,
      'identityToken': credentials.identityToken,
      'identityPoolId': credentials.identityPoolId,
    };

    return await _channel.invokeMethod('upload', params);
  }
}
