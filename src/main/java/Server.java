import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    static class Server1 {

        public String word;

        public Server1(String word) {
            this.word = word;
        }

        @Override
        public String toString() {
            return "Запрос { " +
                    "Слово = '" + word + '\'' +
                    " } ";
        }
    }

    private final int port;
    private final BooleanSearchEngine engine;

    public Server(int port) throws IOException {
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
                    System.out.println("Новое подключение!");
                    System.out.println("Клиент: " + clientSocket.getInetAddress() + " , порт: " + clientSocket.getPort());
                    String json = in.readLine();
                    Server1 r = new Gson().fromJson(json, Server1.class);

                    if (r.word != null && !r.word.isEmpty()) {
                        List<PageEntry> result = this.engine.search(r.word);
                        System.out.println(listToJson(result));
                        out.println(listToJson(result));
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }

    public <T> Object listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(list, listType);
    }
}
