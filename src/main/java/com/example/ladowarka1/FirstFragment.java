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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    int onOffButtonState = 0;

    int counter = 0;

    String POSTResponse = "";
    String POSTResponse2 = "";
    String Text2 = "";
    String Text3 = "";
    String Text4 = "";

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
                    //binding.onoffbutton.setText("OFF");
                    //onOffButtonState = 1;

                    try {
                        SendPOSTChangeState(1, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    //binding.onoffbutton.setText("ON");
                    //onOffButtonState = 0;

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

    public void SendPOSTReadLogs(View view) throws IOException {

        String message = "Connection ERROR";

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try
                {

                    String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<Request>\n  <CLEAR>@@@</CLEAR>\n  <ID_LADOWARKI>@@@</ID_LADOWARKI>\n  <X_RECENT_LOGS_READ>@@@</X_RECENT_LOGS_READ>\n </Request>";
                    int intVal;

                    intVal = 0;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 1;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 2;
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
                    POSTResponse2 = response;

                    line = reader.readLine();

                    while(line != null)
                    {
                        response = response.concat(line);
                        line = reader.readLine();
                    }

                    POSTResponse2 = response;

                    http.disconnect();

                    String s1 = POSTResponse2.toString();

                    Text2 = "";

                    String date1 = "";
                    String date2 = "";
                    String chargingstatus1 = "";
                    String chargingstatus2 = "";
                    String currentkwh1 = "";
                    String currentkwh2 = "";

                    String pattern1 = "<TIMESTAMP_1>";
                    String pattern2 = "</TIMESTAMP_1>";

                    Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    Matcher m = p.matcher(s1);
                    while (m.find()) {
                        date1 = (m.group(1));
                    }

                    pattern1 = "<TIMESTAMP_2>";
                    pattern2 = "</TIMESTAMP_2>";

                    p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    m = p.matcher(s1);
                    while (m.find()) {
                        date2 = (m.group(1));
                    }

                    pattern1 = "<CHARGING_STATUS_1>";
                    pattern2 = "</CHARGING_STATUS_1>";

                    p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    m = p.matcher(s1);
                    while (m.find()) {
                        chargingstatus1 = (m.group(1));
                    }

                    pattern1 = "<CHARGING_STATUS_2>";
                    pattern2 = "</CHARGING_STATUS_2>";

                    p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    m = p.matcher(s1);
                    while (m.find()) {
                        chargingstatus2 = (m.group(1));
                    }

                    pattern1 = "<CURRENT_KWH_1>";
                    pattern2 = "</CURRENT_KWH_1>";

                    p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    m = p.matcher(s1);
                    while (m.find()) {
                        currentkwh1 = (m.group(1));
                    }

                    pattern1 = "<CURRENT_KWH_2>";
                    pattern2 = "</CURRENT_KWH_2>";

                    p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                    m = p.matcher(s1);
                    while (m.find()) {
                        currentkwh2 = (m.group(1));
                    }

                    int currentkwh1_int = Integer.parseInt(currentkwh1);
                    int currentkwh2_int = Integer.parseInt(currentkwh2);

                    int currentkwh_delta_int = currentkwh1_int - currentkwh2_int;


                    // 2021/08/29 23:02:56
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");

                    Date date11 = formatter.parse(date1);
                    Date date22 = formatter.parse(date2);

                    double timeDiff = (double) (date11.getTime() - date22.getTime());
                    timeDiff = (double) timeDiff / (1000*60*60); // h

                    double wattDiff = (double) currentkwh_delta_int / timeDiff;
                    double kWattDiff = (double) wattDiff / 1000;

                    double currentA = (double) wattDiff / 230;

                    String wattDiff_string = String.format("%.2f", wattDiff);
                    String kWattDiff_string = String.format("%.2f", kWattDiff);
                    String currentA_string = String.format("%.2f", currentA);

                    Text2 = "Power[kW]:";
                    Text2 = Text2.concat(kWattDiff_string);

                    handlerSendPOSTReadState.post(new Runnable() {
                        public void run() {
                            binding.textview2.setText(Text2);
                            view.refreshDrawableState();
                        }
                    });

                    Text3 = "Voltage[V]:230";

                    handlerSendPOSTReadState.post(new Runnable() {
                        public void run() {
                            binding.textview3.setText(Text3);
                            view.refreshDrawableState();
                        }
                    });

                    Text4 = "Current[A]:";
                    Text4 = Text4.concat(currentA_string);

                    handlerSendPOSTReadState.post(new Runnable() {
                        public void run() {
                            binding.textview4.setText(Text4);
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
                        UpdateButtonState(view);
                        Thread.sleep(500);
                        SendPOSTReadLogs(view);

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

    public void UpdateButtonState(View view)
    {
        String buttonState = binding.textviewFirst.getText().toString();

        Pattern pattern = Pattern.compile("<(.*?)>");
        String[] result = pattern.split(buttonState);

        onOffButtonState = Integer.parseInt(result[1]);

        if (onOffButtonState==0)
            binding.onoffbutton.setText("OFF");
        if (onOffButtonState==1)
            binding.onoffbutton.setText("ON");

        view.refreshDrawableState();
    }



}
