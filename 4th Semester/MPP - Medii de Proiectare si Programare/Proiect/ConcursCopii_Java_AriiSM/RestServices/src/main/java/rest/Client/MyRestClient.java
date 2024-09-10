package rest.Client;

import client.Domain_REST.Concurs;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class MyRestClient {
    RestClient restClient = RestClient.builder().requestInterceptor(new CustomRestClientInterceptor()).build();
    public static final String URL = "http://localhost:8080/concurs";

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Concurs[] findAll() {
        return execute(() -> restClient.get().uri(URL).retrieve().body(Concurs[].class));
    }
    public Concurs findOne(Integer id) {
        return execute(() -> restClient.get().uri(String.format("%s/%s", URL, id)).retrieve().body(Concurs.class));
    }

    public void save(Concurs concurs) {
        execute(() -> restClient.post().uri(URL).contentType(APPLICATION_JSON).body(concurs).retrieve().body(String.class));
    }

    public void update(Integer id, Concurs concurs) {
        execute(() -> restClient.put().uri(String.format("%s/%s", URL, id)).contentType(APPLICATION_JSON).body(concurs).retrieve().body(Concurs.class));
    }

    public void delete(Integer id){
        execute(() -> restClient.delete().uri(String.format("%s/%s", URL, id)).retrieve().body(Concurs.class));
    }

    public static class CustomRestClientInterceptor implements ClientHttpRequestInterceptor{
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



