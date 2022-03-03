package com.cocoon.controller;

import com.cocoon.util.payment_old_one.Payment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PaymentRestController {

    private WebClient webClient = WebClient.builder().baseUrl("https://api.yapily.com").build();

    @GetMapping("/payment-auth-requests")
    public Mono<Object> createWebClient() {
        Payment payment1 = new Payment();
        System.out.println("payment1.toString() = " + payment1.toString());
        return webClient
                .post()
                .uri("/payment-auth-requests")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Basic ODZlM2ZmZWEtNjFkOC00MTQ5LTk2NmMtM2YzMjFiZWJhYTEyOmZkYTdkZGFlLTE5OWEtNDM3ZS1iMTRkLTI2ZmRjNGI2MmU4Nw==")
                .body(Mono.just(payment1), Payment.class)
                .retrieve()
                .bodyToMono(Object.class);
    }

    @GetMapping("/payments/{id}/details")
    public Mono<Object> readMonoWithWebClient(@PathVariable("id") String id){
        return webClient
                .get()
                .uri("/payments/{id}/details", id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Basic ODZlM2ZmZWEtNjFkOC00MTQ5LTk2NmMtM2YzMjFiZWJhYTEyOmZkYTdkZGFlLTE5OWEtNDM3ZS1iMTRkLTI2ZmRjNGI2MmU4Nw==")
                .header("Consent", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJJTlNUSVRVVElPTiI6Im1vZGVsby1zYW5kYm94IiwiQ09OU0VOVCI6ImE4ZTRmZTAxLTQ2ZmMtNDAwYy05OTdjLWNjMzMyNzEzOGY2ZiIsIkFQUExJQ0FUSU9OX1VTRVJfSUQiOiJzaW5nbGUtcGF5bWVudC10dXRvcmlhbCIsIlVTRVIiOiIzZWM1YTYxNy0zNDdhLTQ5ZTctOTIxYS1mZGY5MjlmNjY4ZWEifQ.v2BI0ry1ogn2qnvIChaHx3Y3MEjZ-E3FCdnmwHVp2MlRfoC-9ZD9ZHxeuSF02T_DgC1E-EuR82243U9PcnceVw")
                .retrieve()
                .bodyToMono(Object.class);

    }

    @GetMapping("/institutions")
    public Flux<Object> readMonoWithInstitutions(){
        return webClient
                .get()
                .uri("/institutions")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Basic ODZlM2ZmZWEtNjFkOC00MTQ5LTk2NmMtM2YzMjFiZWJhYTEyOmZkYTdkZGFlLTE5OWEtNDM3ZS1iMTRkLTI2ZmRjNGI2MmU4Nw==")
                .retrieve()
                .bodyToFlux(Object.class);
    }
}
