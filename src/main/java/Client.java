import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Client {
    static int port = 8989;


    public static void main(String[] args) throws IOException {
        try (
                Socket clientSocket = new Socket("localhost", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        ) {
            out.println("бизнес");
            String resp = in.readLine();
            System.out.println(resp);
        }
    }

    public static List<PageEntry> jsonToList(String json) {

        List<PageEntry> list = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(json);
            JSONArray jsonArray = (JSONArray) obj;

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            for (Object oneObject : jsonArray) {

                JSONObject jsonObject = (JSONObject) oneObject;
                PageEntry pageEntry = gson.fromJson(String.valueOf(jsonObject), PageEntry.class);
                list.add(pageEntry);
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}