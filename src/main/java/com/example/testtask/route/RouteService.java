package com.example.testtask.route;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class RouteService {
    private final Map<Integer, List<Integer>> routeStops = new HashMap<>();

    public RouteResponse checkRoute(Integer from, Integer to) {
        RouteResponse response = new RouteResponse();
        response.setFrom(from);
        response.setTo(to);
        response.setDirect(hasDirectRoute(from, to));

        return response;
    }

    public void loadRoutesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processRouteLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processRouteLine(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 2) {
            // Некорректный формат строки маршрута
            return;
        }

        int routeId = Integer.parseInt(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            int stopId = Integer.parseInt(parts[i]);
            routeStops.computeIfAbsent(routeId, k -> new ArrayList<>()).add(stopId);
        }
    }

    public boolean hasDirectRoute(int from, int to) {
        return routeStops.values().parallelStream().anyMatch(stops -> stops.contains(from) && stops.contains(to) &&
                findStopIndex(stops, from) < findStopIndex(stops, to));
    }

    private int findStopIndex(List<Integer> stops, int stopId) {
        return IntStream.range(0, stops.size()).filter(i -> stops.get(i) == stopId).findFirst().orElse(-1);
    }
}
