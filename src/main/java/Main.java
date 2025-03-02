import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        StartServer startServer = new StartServer(8989);
        startServer.start();
    }

    static class StartServer {
        private int port = 8989;
        private BooleanSearchEngine engine;

        public StartServer(int port) throws IOException {
            this.port = port;
            engine = new BooleanSearchEngine(new File("pdfs"));
        }

        public void start() {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                System.out.println("\nЗапускается сервер " + this.port + "... \nСервер запущен\n");
                while (true) {
                    try (
                            Socket clientSocket = serverSocket.accept();
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        String word = in.readLine();
                        List<PageEntry> hyt = this.engine.search(word);
                        String res = new GsonBuilder().create().toJson(hyt);
                        out.println(res);

                    }
                }
            } catch (IOException e) {
                System.out.println("Не могу стартовать сервер!");
                e.printStackTrace();
            }
        }


        public static <T> Object listToJson(List<T> list) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Type listType = new TypeToken<List<T>>() {
            }.getType();
            return gson.toJson(list, listType);
        }
    }

}
