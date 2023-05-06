package com.nekonex.services.employee.utils;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;

@Filter(patterns = "/**")
@Slf4j
@Singleton
public class LoggingClientFilter implements HttpClientFilter {

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        String traceId = request.getHeaders().contains("X-TRACE-ID") ?
                request.getHeaders().get("X-TRACE-ID") :
                request.getAttribute("traceId", String.class).orElse(null);
        if (traceId == null){
            traceId = MDC.get("traceId");
        }
        log.info("Request URL: {}, TraceId: {}", request.getUri(), traceId);
        if (traceId != null) {
            request.getHeaders().add("X-TRACE-ID", traceId);
        }
        return chain.proceed(request);
    }
}