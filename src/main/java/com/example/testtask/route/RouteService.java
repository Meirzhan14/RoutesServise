package com.example.testtask.route;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteService {
    private Map<Integer, List<Integer>> routeStops;

    public RouteService() {
        routeStops = new HashMap<>();
    }

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
        List<Integer> stops = routeStops.computeIfAbsent(routeId, k -> new ArrayList<>());

        for (int i = 1; i < parts.length; i++) {
            int stopId = Integer.parseInt(parts[i]);
            stops.add(stopId);
        }
    }

    public boolean hasDirectRoute(int from, int to) {
        for (List<Integer> stops : routeStops.values()) {
            if (stops.contains(from) && stops.contains(to)) {
                int fromIndex = findStopIndex(stops, from);
                int toIndex = findStopIndex(stops, to);
                if (fromIndex != -1 && toIndex != -1 && fromIndex < toIndex) {
                    return true;
                }
            }
        }
        return false;
    }

    private int findStopIndex(List<Integer> stops, int stopId) {
        int index = 0;
        for (int stop : stops) {
            if (stop == stopId) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
