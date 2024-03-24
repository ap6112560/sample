package com.example.completable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class ProxyService {
    @RequestMapping(value = "/{path}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyRequest(@PathVariable String path, @RequestBody(required = false) String requestBody, HttpMethod method, @RequestHeader HttpHeaders headers) {
        String targetUrl = headers.getFirst("External-Endpoint");
        if (targetUrl == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("External-Endpoint header not provided.");
        }

        targetUrl += "/" + path;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addAll(headers);
        httpHeaders.remove("External-Endpoint"); // Remove the external endpoint header before forwarding
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(targetUrl, method, requestEntity, String.class);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(responseEntity.getBody());
    }
}
