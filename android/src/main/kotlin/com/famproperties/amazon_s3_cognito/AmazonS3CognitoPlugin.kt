package com.famproperties.amazon_s3_cognito

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.service.ServiceAware
import io.flutter.embedding.engine.plugins.service.ServicePluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

import java.io.File


class AmazonS3CognitoPlugin :FlutterPlugin,MethodCallHandler, ActivityAware , ServiceAware {

    private lateinit var channel : MethodChannel
    private lateinit var eventChannel : EventChannel

    private lateinit var context: Context
    private lateinit var activity: Activity


    override fun onMethodCall(call: MethodCall, result: Result) {
        val filePath = call.argument<String>("filePath")
        val bucket = call.argument<String>("bucket")
        val key = call.argument<String>("key")
        val identityId = call.argument<String>("identityId")
        val identityToken = call.argument<String>("identityToken")
        val identityPoolId = call.argument<String>("identityPoolId")
        val region = call.argument<String>("region")

        val awsHelper = AwsHelper()


        if(call.method.equals("upload")) {

            val file = Uri.parse(filePath).path?.let { File(it) }

            TransferNetworkLossHandler.getInstance(context.applicationContext)

            val awsRegion = RegionHelper(region!!).getRegionName();
            val credentials = CognitoCredentials(identityId!!, identityToken!!, identityPoolId!!);
            val transferUtility = awsHelper.getTransferUtility(context, credentials, awsRegion)

            val transferObserver = transferUtility.upload(bucket, key, file)

            transferObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    if (state == TransferState.COMPLETED) {
                        result.success("")
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
                override fun onError(id: Int, ex: Exception) {
                    result.error(id.toString(), "error", ex)
                }
            })

        } else {
            result.notImplemented();
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "amazon_s3_cognito")
        channel.setMethodCallHandler(this)

        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "amazon_s3_cognito_images_upload_steam")

        //Factory.setup(this, flutterPluginBinding.binaryMessenger)
        context = flutterPluginBinding.applicationContext

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}

    override fun onDetachedFromActivity() {}

    override fun onAttachedToService(binding: ServicePluginBinding) {
        context = binding.service.applicationContext
    }

    override fun onDetachedFromService() {}
}
