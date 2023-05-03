package com.nekonex.services.department.utils;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

@Filter
@Slf4j
@Singleton
public class LoggingClientFilter implements HttpClientFilter {

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        String traceId = request.getAttribute("traceId", String.class).orElse(null);
        log.info("Request URL: {}, TraceId: {}", request.getUri(), traceId);
        if (traceId != null) {
            request.getHeaders().add("X-TRACE-ID", traceId);
        }
        return chain.proceed(request);
    }
}