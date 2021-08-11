package com.example.ladowarka1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ladowarka1.databinding.FragmentFirstBinding;

//import sun.net.www.http.HttpClient;

import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    int onOffButtonState = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.onoffbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onOffButtonState==0)
                {
                    binding.onoffbutton.setText("OFF");
                    onOffButtonState = 1;

                    try
                    {
                        SendPOST(1);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    binding.onoffbutton.setText("ON");
                    onOffButtonState = 0;

                    try
                    {
                        SendPOST(0);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                view.refreshDrawableState();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void SendPOST(int state) throws IOException {

        final String[] logText = {"aaa0"};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try
                {

                    String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<Request>\n  <CLEAR>@@@</CLEAR>\n  <ID_LADOWARKI>@@@</ID_LADOWARKI>\n  <STATUS_LADOWARKI_READ>@@@</STATUS_LADOWARKI_READ>\n  <STATUS_LADOWARKI_WRITE>@@@</STATUS_LADOWARKI_WRITE>\n </Request>";
                    int intVal;

                    logText[0] = "aaa1";

                    intVal = 0;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 1;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = 1;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));
                    intVal = state;
                    data=data.replaceFirst("@@@",Integer.toString(intVal));

                    logText[0] = "aaa2";

                    URL url = new URL("http://www.greenyy.webd.pro/interfaces/ladowarka.php");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setRequestProperty("Content-Type", "application/xml");

                    logText[0] = "aaa3";
                    //binding.textviewFirst.setText(data);

                    byte[] out = data.getBytes(StandardCharsets.UTF_8);

                    logText[0] = "aaa4";

                    try {
                        OutputStream stream = http.getOutputStream();
                        stream.write(out);
                    }
                    catch (Exception e)
                    {
                        binding.textviewFirst.setText(e.toString());
                        //binding.textviewFirst.setText(data);
                    }

                    //binding.textviewFirst.setText("aaa6");

                    logText[0] = http.getResponseMessage();

                    //binding.textviewFirst.setText("aaa7");

                    //binding.textviewFirst.setText(data);

                    http.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });

        thread.start();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        binding.textviewFirst.setText(logText[0]);
    }
}
