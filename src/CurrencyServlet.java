import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amount = request.getParameter("amount");

        String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + from;

        URL url = new URL(apiUrl);
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        StringBuilder json = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        reader.close();

        // Very simple parsing (demo purpose)
        String search = "\"" + to + "\":";
        int index = json.indexOf(search);
        int start = index + search.length();
        int end = json.indexOf(",", start);

        double rate =
                Double.parseDouble(json.substring(start, end));
        double result = rate * Double.parseDouble(amount);

        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(result));
    }
}
