package com.nekonex.services.department.utils;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Filter
@Slf4j
@Singleton
public class TraceIdServerFilter implements HttpServerFilter {

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        log.info("Request URL: {}", request.getUri());
        String uuid = request.getHeaders().contains("X-TRACE-ID") ?
                request.getHeaders().get("X-TRACE-ID") :
                UUID.randomUUID().toString();
        request.setAttribute("traceId", uuid);
        return Flux.from(chain.proceed(request)).map(
                mutableHTTPResponse -> {
                    MDC.put("traceId", uuid);
                    mutableHTTPResponse.getHeaders().add("X-TRACE-ID", uuid);
                    log.debug(mutableHTTPResponse.getBody(String.class).orElse("empty"));
                    return mutableHTTPResponse;
                }
        );
    }
}