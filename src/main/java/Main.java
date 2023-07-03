import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(engine.search("бизнес"));

        StartServer startServer = new StartServer(8989);
        startServer.start();
    }

}
