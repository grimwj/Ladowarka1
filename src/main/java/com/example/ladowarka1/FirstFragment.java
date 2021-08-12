package com.example.ladowarka1;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ladowarka1.databinding.FragmentFirstBinding;

//import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    int onOffButtonState = 0;

    int counter = 0;

    String POSTResponse = "";

    Handler handlerSendPOSTChangeState = new Handler();
    Handler handlerSendPOSTReadState = new Handler();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.onoffbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onOffButtonState == 0) {
                    binding.onoffbutton.setText("OFF");
                    onOffButtonState = 1;

                    try {
                        SendPOSTChangeState(1, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    binding.onoffbutton.setText("ON");
                    onOffButtonState = 0;

                    try {
                        SendPOSTChangeState(0, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                view.refreshDrawableState();
            }
        });


        try {
            CyclicOperations(view);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void SendPOSTChangeState(int state, View view) throws IOException {

        final String[] logText = {"default"};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try
                {

                    String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<Request>\n  <CLEAR>@@@</CLEAR>\n  <ID_LADOWARKI>@@@</ID_LADOWARKI>\n  <STATUS_LADOWARKI_READ>@@@</STATUS_LADOWARKI_READ>\n  <STATUS_LADOWARKI_WRITE>@@@</STATUS_LADOWARKI_WRITE>\n </Request>";
                    int intVal;

                    intVal = 0;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 1;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 1;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = state;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));

                    URL url = new URL("http://www.greenyy.webd.pro/interfaces/ladowarka.php");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setRequestProperty("Content-Type", "application/xml");

                    byte[] out = data.getBytes(StandardCharsets.UTF_8);

                    try {
                        OutputStream stream = http.getOutputStream();
                        stream.write(out);
                    }
                    catch (Exception e)
                    {
                    }

                    POSTResponse = http.getResponseMessage();

                    http.disconnect();

                    handlerSendPOSTChangeState.post(new Runnable() {
                        public void run() {
                            //binding.textviewFirst.setText(POSTResponse);
                            view.refreshDrawableState();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void SendPOSTReadState(View view) throws IOException {

        String message = "Connection ERROR";

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try
                {

                    String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<Request>\n  <CLEAR>@@@</CLEAR>\n  <ID_LADOWARKI>@@@</ID_LADOWARKI>\n  <STATUS_LADOWARKI_READ>@@@</STATUS_LADOWARKI_READ>\n </Request>";
                    int intVal;

                    intVal = 0;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 1;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 1;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));

                    URL url = new URL("http://www.greenyy.webd.pro/interfaces/ladowarka.php");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setRequestProperty("Content-Type", "application/xml");

                    byte[] out = data.getBytes(StandardCharsets.UTF_8);

                    try {
                        OutputStream stream = http.getOutputStream();
                        stream.write(out);
                    }
                    catch (Exception e)
                    {
                    }

                    ///*
                    InputStream inputStream = http.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line = "";
                    String response = "";
                    POSTResponse = response;

                    line = reader.readLine();

                    while(line != null)
                    {
                        response = response.concat(line);
                        line = reader.readLine();
                    }

                    POSTResponse = response;

                    //response = response.concat(line);

                    //*/
                    http.disconnect();

                    handlerSendPOSTReadState.post(new Runnable() {
                        public void run() {
                            binding.textviewFirst.setText(POSTResponse);
                            //binding.textviewFirst.setText(data);
                            //binding.textviewFirst.setText(Integer.toString(counter));
                            view.refreshDrawableState();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void CyclicOperations(View view) throws IOException
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {

                        SendPOSTReadState(view);

                        counter++;
                        handlerSendPOSTReadState.post(new Runnable() {
                            public void run() {
                                //binding.textviewFirst.setText(Integer.toString(counter));
                                //view.refreshDrawableState();
                            }
                        });

                        Thread.sleep(500);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }


}
