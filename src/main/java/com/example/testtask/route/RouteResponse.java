package com.example.testtask.route;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class RouteResponse {
    private Integer from;
    private Integer to;
    private boolean direct;
}
