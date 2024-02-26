package com.example.testtask.route;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RouteService {
    private final Map<Integer, Map<Integer, Integer>> routeStops;
    private final Map<Integer, Set<Integer>> stopRoutes;

    public RouteService() {
        routeStops = new HashMap<>();
        stopRoutes = new HashMap<>();
        loadRoutesFromFile("C:\\test\\routes.txt");
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
        Map<Integer, Integer> stopIDIndexMap = new HashMap<>();

        for (int i = 1; i < parts.length; i++) {
            int stopId = Integer.parseInt(parts[i]);
            stopIDIndexMap.put(stopId, i);

            routeStops.put(routeId, stopIDIndexMap);

            stopRoutes.computeIfAbsent(stopId, k -> new HashSet<>()).add(routeId);
        }
    }

    public boolean hasDirectRoute(int from, int to) {
        if (!stopRoutes.containsKey(from) || !stopRoutes.containsKey(to)) {
            return false;
        }

        Set<Integer> routesFrom = stopRoutes.get(from);
        Set<Integer> routesTo = stopRoutes.get(to);

        routesFrom.retainAll(routesTo);

        if (!routesFrom.isEmpty()) {
            for (int routeID : routesFrom) {
                Map<Integer, Integer> stopIDIndexMap = routeStops.get(routeID);

                return stopIDIndexMap.get(from) < stopIDIndexMap.get(to);
            }
        }
        return false;
    }
}
