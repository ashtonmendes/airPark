package com.visahackathon.airpark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.visahackathon.airpark.data.models.MerchantLocatorServiceResponse;
import com.visahackathon.airpark.data.remote.ApiUtils;
import com.visahackathon.airpark.data.remote.MLService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingActivity extends AppCompatActivity {

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode;

    //a list to store all the products
    List<Parking> parkingList;

    //the recyclerview
    RecyclerView recyclerView;


    private MLService mService;
    ParkingAdapter mAdapter;
    String bodyJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        LatLng position = getIntent().getExtras().getParcelable("destLocation");
        CharSequence distanceAmount = getIntent().getExtras().getCharSequence("distanceAmount");
        Log.d("ParkingDetails", "@@@ position "+position+" "+distanceAmount.toString());


        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //retrofit merchant locator api call
        mService = ApiUtils.getMLService();


        mService.getMerchantLocatorDetails().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    // mAdapter.updateAnswers(response.body().getItems());
                    Log.d("ParkingActivity", "@@@ posts loaded from API");
                    Log.d("RetrofitResponse", "@@@ response"+response.toString());


                    bodyJSON = response.body();
                    Log.d("RetrofitResponse", "@@@ response 11"+bodyJSON);
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // showErrorMessage();
                Log.d("ParkingActivity", "error loading from API");

            }
        });

        String merchantName ="";
        try {
            jsonNode = objectMapper.readTree(bodyJSON);

            merchantName = jsonNode.get("merchantLocatorServiceResponse.response[0].responseValues.visaStoreName").textValue();

            Log.d("ParkingActivity", "@@@@ merchantName "+merchantName);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //initializing the productlist
        parkingList = new ArrayList<Parking>();


         //Populating Data
        //adding some items to our list
        parkingList.add(
                new Parking(
                        "STARBUCKS",
                        "1509 S LAMAR BLVD STE 100, AUSTIN, TX",
                        "Distance : 1.84 km",
                        "WaitTime: 4.3 Minutes",
                        "Price : Free",
                        R.drawable.parking));

        parkingList.add(
                new Parking(
                        "STARBUCKS",
                        "1509 S LAMAR BLVD STE 100, AUSTIN, TX ",
                        "Distance : 1.84 km",
                        "WaitTime: 4.3 Minutes",
                        "Price : Free",
                        R.drawable.background));

        parkingList.add(
                new Parking(
                        "STARBUCKS",
                        "1509 S LAMAR BLVD STE 100, AUSTIN, TX ",
                        "Distance : 1.84 km",
                        "WaitTime: 4.3 Minutes",
                        "Price : Free",
                        R.drawable.parking));

        parkingList.add(
                new Parking(
                        "STARBUCKS",
                        "1509 S LAMAR BLVD STE 100, AUSTIN, TX ",
                        "Distance : 1.84 km",
                        "WaitTime: 4.3 Minutes",
                        "Price : Free",
                        R.drawable.parking));

        //creating recyclerview adapter
        ParkingAdapter adapter = new ParkingAdapter(this, parkingList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }



}