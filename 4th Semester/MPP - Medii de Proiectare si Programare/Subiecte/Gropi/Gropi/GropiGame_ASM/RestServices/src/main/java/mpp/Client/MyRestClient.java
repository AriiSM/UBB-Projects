package mpp.Client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class MyRestClient {
    RestClient restClient = RestClient.builder().requestInterceptor(new CustomRestClientInterceptor()).build();
    public static final String URL = "http://localhost:8080/jucatoriGames";

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String findOneJucator(Integer id) {
        return execute(() -> restClient.get().uri(String.format("%s/jucator/%s", URL, id)).retrieve().body(String.class));
    }

    public void updateConfigurare(Integer id, Map<String, String> updates) {
        execute(() -> restClient.put().uri(String.format("%s/configurare/%s", URL, id)).contentType(APPLICATION_JSON).body(updates).retrieve().body(String.class));
    }
    public static class CustomRestClientInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sendind a " + request.getMethod()+" request to "+request.getURI()+" and body["+new String(body)+"]");
            ClientHttpResponse response = null;
            try {
                response = execution.execute(request, body);
                System.out.println("Got response code "+response.getStatusText());
            } catch (IOException e) {
                System.out.println("Exception ... "+e.getMessage());
            }
            return response;
        }
    }

}
