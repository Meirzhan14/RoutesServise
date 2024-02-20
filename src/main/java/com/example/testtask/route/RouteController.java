package com.example.testtask.route;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/direct")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
        routeService.loadRoutesFromFile("C:\\test\\routes.txt");
    }

    @GetMapping
    public RouteResponse checkRoute(@RequestParam Integer from, @RequestParam Integer to) {
        return routeService.checkRoute(from, to);
    }
}
