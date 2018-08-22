package com.example.serviciossms.service;

import android.telephony.SmsManager;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EnvioSMS {

    public EnvioSMS() {
        Log.i(":::::231::::::::","Envio");
        Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        consultarMensajes();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("Timer", "Termino");
                    }
                });
    }

    private void consultarMensajes() {
        Log.i(":::::232::::::::","Envio");
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAJRKEVKWC5FFFEJ7Q", "/ZC7bsfK2gQOAi0fh8YZpnvMQ2gnIuzoM+toZBa9");
        final AmazonSQSAsyncClient client = new AmazonSQSAsyncClient(awsCreds);
        client.setRegion(Region.getRegion(Regions.fromName("us-west-2")));
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withMessageAttributeNames("All")
                .withQueueUrl("sms.fifo");

        Future<ReceiveMessageResult> result = client.receiveMessageAsync(receiveMessageRequest, new AsyncHandler<ReceiveMessageRequest, ReceiveMessageResult>() {
            @Override
            public void onError(Exception exception) {
                    Log.i("Mensaje de eror " , exception.getMessage());
            }

            @Override
            public void onSuccess(ReceiveMessageRequest request, ReceiveMessageResult receiveMessageResult) {

                if (receiveMessageResult.getMessages().size() > 0) {
                    Message message = receiveMessageResult.getMessages().get(0);
                    Log.i("sqs", Arrays.toString(message.getMessageAttributes().values().toArray()));
                    String phone = message.getMessageAttributes().get("phone").getStringValue();
                    String msg = message.getBody();
                    sendSMS(phone, msg);
                    Log.i("sendSMS", "Eliminando Mensaje");
                    client.deleteMessageAsync(new DeleteMessageRequest("sms.fifo", message.getReceiptHandle()));
                }

            }
        });
    }

    private void sendSMS(String phoneNumber, String message) {
        Log.i("sendSMS", String.format("%s--%s", phoneNumber, message));
        SmsManager.getDefault().sendTextMessage(phoneNumber, null,
                message, null, null);
    }
}
