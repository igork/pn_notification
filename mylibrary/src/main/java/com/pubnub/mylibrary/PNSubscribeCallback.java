package com.pubnub.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult;

public class PNSubscribeCallback extends SubscribeCallback {

    final String channelName;
    Context context;
    Activity activity;

    public PNSubscribeCallback(String name){
        channelName = name;
    }

    public PNSubscribeCallback(String name, Activity activity){

        this.channelName = name;
        this.context = activity.getApplicationContext();
        this.activity = activity;
    }

    @Override
    public void signal(PubNub pubnub, PNSignalResult result){
        System.out.println("SIGNAL: category:" + result.getMessage());
    }


    @Override
    public void status(PubNub pubnub, PNStatus status) {
        if (status.getCategory() == PNStatusCategory.PNUnknownCategory) {
            System.out.println(status.getErrorData());
        }
        System.out.println("STATUS: category:" + status.getCategory().name());


/*
        JsonObject messageJsonObject = new JsonObject();
        //position.addProperty("text", "Hello From Java SDK");
        messageJsonObject.addProperty("igor", " some data " + 777);


        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
            // This event happens when radio / connectivity is lost
        }

        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

            // Connect event. You can do stuff like publish, and know you'll get it.
            // Or just use the connected event to confirm you are subscribed for
            // UI / internal notifications, etc

            if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // Check whether request successfully completed or not.
                        if (!status.isError()) {

                            // Message successfully published to specified channel.
                        }
                        // Request processing failed.
                        else {

                            // Handle message publish error. Check 'category' property to find out possible issue
                            // because of which request did fail.
                            //
                            // Request can be resent using: [status retry];
                        }
                    }
                });
            }
        }
        else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

            // Happens as part of our regular operation. This event happens when
            // radio / connectivity is lost, then regained.
        }
        else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

            // Handle messsage decryption error. Probably client configured to
            // encrypt messages and on live data feed it received plain text.
        }
        */
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

        JsonElement msg = message.getMessage();
        System.out.println("MESSAGE: " + msg);

        String messagePublisher = message.getPublisher();
        System.out.println("Message publisher: " + messagePublisher);
        System.out.println("Message Payload: " + message.getMessage());
        System.out.println("Message Subscription: " + message.getSubscription());
        System.out.println("Message Channel: " + message.getChannel());
        System.out.println("Message timetoken: " + message.getTimetoken());

        if (message.getUserMetadata()!=null) {
            System.out.println("Message metadata: " + message.getUserMetadata().getAsString());
        }

        //Toast..makeText(this, "Refresh", Toast.LENGTH_LONG).show();

        showToast(msg);
        /*

        // Handle new message stored in message.message
        if (message.getChannel() != null) {
            // Message has been received on channel group stored in
            // message.getChannel()
        }
        else {
            // Message has been received on channel stored in
            // message.getSubscription()
        }

        JsonElement receivedMessageObject = message.getMessage();
        System.out.println("Received message content: " + receivedMessageObject.toString());
        // extract desired parts of the payload, using Gson
        String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
        System.out.println("msg content: " + msg);

        */

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */

    }

    public void wrong(JsonElement msg){
        if (context!=null) {
            Toast.makeText(context,msg.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void showToast(final JsonElement msg){
        if (context!=null) {

            new Thread()
            {
                public void run()
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            //Do your UI operations like dialog opening or Toast here
                            Toast.makeText(context,msg.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }.start();


        }
    }

    public void showToast2(final JsonElement msg){
        if (context!=null) {

            Handler handler =  new Handler(context.getMainLooper());
            handler.post( new Runnable(){
                public void run(){
                    Toast.makeText(context,msg.toString(),Toast.LENGTH_LONG).show();
                }
            });
            Toast.makeText(context,msg.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        System.out.println("PRESENCE: channel:" + presence.getChannel() + " event:" + presence.getEvent());
    }

    @Override
    public void user(PubNub pubnub, PNUserResult result) {
        System.out.println("USER: name:" + result.getUser().getName());
    }

    @Override
    public void space(PubNub pubnub, PNSpaceResult pnSpaceResult) {
        System.out.println("SPACE: ");
    }

    @Override
    public void membership(PubNub pubnub, PNMembershipResult pnMembershipResult) {
        System.out.println("MEMBERSHIP: ");
    }
}
