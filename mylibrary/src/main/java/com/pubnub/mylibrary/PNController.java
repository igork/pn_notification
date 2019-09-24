package com.pubnub.mylibrary;

import android.app.Activity;
import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PNController {

    PubNub pubnub;

    String channelName; // = "demo_tutorial";
    String subscribeKey;
    String publishKey;

    Context context;
    Activity activity;

    public PNController(Activity activity){

        this.activity = activity;
        this.context = activity.getApplicationContext();
        init();

    }

    private void init(){
        //init
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-93dfd156-c05e-11e9-be0f-1ea63a606bf6");
        pnConfiguration.setPublishKey("pub-c-3557824b-f648-404b-a331-b150b7d80c3a");

        pubnub = new PubNub(pnConfiguration);
    }

    private void getSettings(){
        String settings = getJsonFile(context);

        JsonElement element = new JsonPrimitive(settings);
        JsonObject result = element.getAsJsonObject();

        element = result.get("subscribeKey");
        subscribeKey = element.getAsString();

        element = result.get("publishKey");
        publishKey = element.getAsString();

        element = result.get("channedlName");
        channelName = element.getAsString();

    }

    public void setChannelName(String channelName){
        this.channelName = channelName;
    }

    private static String getJsonFile(Context context){

        String json;

        try {
            InputStream is = context.getAssets().open("pn_services.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        //Log.e("data", json);
        return json;

    }

    private static String getDate(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);

    }

    public void subscribe(){
        try {

            pubnub.addListener(new PNSubscribeCallback(channelName, activity));

            pubnub.subscribe()
                    .channels(Arrays.asList(channelName))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static int num =0;

    public void test(){

        JsonObject position = new JsonObject();
        //position.addProperty("text", "Hello From Java SDK");
        position.addProperty("igor", getDate() + " some data " + num);

        pubnub.publish()
                .message(position)
                .channel("pubnub_onboarding_channel")
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle response
                    }
                });

    }

    public void publish(JSONObject data){
        pubnub.publish()
                .message(data)
                .channel(channelName)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status)
                    {
                        System.out.println("On Response" + result.toString());
                    }
                });
    }

}
