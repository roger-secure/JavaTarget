import com.google.gson.Gson;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.net.URI;


//This code grabs the forecast file and sends it Google message


public class GM4 {
  private static final String URL = "https://chat.googleapis.com/v1/spaces/AAAAtcpM7bU/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=Ze29u4dtR4G_STJxppK70r2gSTS4A5HqNH_802K6wzQ%3D";
  private static final Gson gson = new Gson();
  private static final HttpClient client = HttpClient.newHttpClient();

  public static void main(String[] args) throws Exception {
    // Read the contents of the file
    Path path = Paths.get("output/forecast.txt");
    String content = Files.readString(path);

    String message = gson.toJson(Map.of("text", content));

    HttpRequest request = HttpRequest.newBuilder(
        URI.create(URL))
        .header("accept", "application/json; charset=UTF-8")
        .POST(HttpRequest.BodyPublishers.ofString(message))
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());
  }
}
