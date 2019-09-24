package com.pubnub.mypubnup;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.google.gson.JsonObject;

import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    PubNub pubnub;
    int    num =0;

    final String channelName = "demo_tutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        String json = getSettings(getApplicationContext());
        System.out.println(json);


        //init
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-93dfd156-c05e-11e9-be0f-1ea63a606bf6");
        pnConfiguration.setPublishKey("pub-c-3557824b-f648-404b-a331-b150b7d80c3a");

        pubnub = new PubNub(pnConfiguration);
        ////////////////////////

        //000000000
        /*
        for( int i=0; i<5; i++) {
            publish("" + i);
        }
        */

        /*
        https://admin.pubnub.com/#/user/517801/account/517759/app/35313219/key/664247/block/51030/editor/49832

        function react
         */


        subscribe();

        for (int i=0; i<5; i++) {
            publish();
        }

        Button button = (Button) findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                //for (int i=0; i<5; i++) {
                    publish();
                //}
            }
        });


    }
    public static String getSettings(Context context){

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

    class otherPNSubscribeCallback extends PNSubscribeCallback{

        public otherPNSubscribeCallback(String channel, AppCompatActivity activity){
            super(channel,activity);
        }
        @Override
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
                                Toast.makeText(context,msg.toString()+"+++++",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }.start();


            }

        }

    }

    public void subscribe(){
        try {

            pubnub.addListener(new otherPNSubscribeCallback(channelName, this));

            pubnub.subscribe()
                    .channels(Arrays.asList(channelName))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish000(String iper){

        JsonObject position = new JsonObject();
        //position.addProperty("text", "Hello From Java SDK");
        position.addProperty("igor", getDate() + " some data " + iper);

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

    public void publish(){
        /* Publish a simple message to the demo_tutorial channel */
        JSONObject data = new JSONObject();

        try {
            /*
            data.put("color", "blue");
            data.put("time",getDate());
            data.put("number",""+num++);

             */
            data.put("title","hello_from_java");
            data.put("description","description" + num++);
            data.put("time",getDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public String getDate(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);

    }


}
