package com.example.testtask.route;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class RouteService {
    private final Map<Integer, List<Integer>> routeStops = new HashMap<>();
    boolean hasDirect = false;

    public RouteResponse checkRoute(Integer from, Integer to) {
        hasDirect = false;
        RouteResponse response = new RouteResponse();
        response.setFrom(from);
        response.setTo(to);

        response.setDirect(loadRoutesFromFile("C:\\test\\routes.txt", from, to));

        return response;
    }

    public boolean loadRoutesFromFile(String filePath, int from, int to) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && !hasDirect) {
                processRouteLine(line, from, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hasDirect;
    }

    private void processRouteLine(String line, int from, int to) {
        String[] parts = line.split(" ");
        if (parts.length < 2) {
            // Некорректный формат строки маршрута
            return;
        }

        int routeId = Integer.parseInt(parts[0]);
        Set<Integer> stops = new HashSet<>();

        for (int i = 1; i < parts.length; i++) {
            int stopId = Integer.parseInt(parts[i]);
            stops.add(stopId);

            routeStops.computeIfAbsent(routeId, k -> new ArrayList<>()).add(stopId);
        }

        hasDirect = hasDirectRoute(from, to, routeStops.get(routeId));
    }

    public boolean hasDirectRoute(int fromStop, int toStop, List<Integer> stops) {
        return stops.contains(fromStop) && stops.contains(toStop) &&
                findStopIndex(stops, fromStop) < findStopIndex(stops, toStop);
    }

    private int findStopIndex(List<Integer> stops, int stopId) {
        return IntStream.range(0, stops.size()).filter(i -> stops.get(i) == stopId).findFirst().orElse(-1);
    }
}
