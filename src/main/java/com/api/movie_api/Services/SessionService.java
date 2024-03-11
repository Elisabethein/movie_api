package com.api.movie_api.Services;

import com.api.movie_api.Entities.History;
import com.api.movie_api.Entities.Movie;
import com.api.movie_api.Entities.Seat;
import com.api.movie_api.Entities.Session;
import com.api.movie_api.Repositories.HistoryRepository;
import com.api.movie_api.Repositories.MovieRepository;
import com.api.movie_api.Repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final HistoryRepository historyRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, MovieRepository movieRepository, HistoryRepository historyRepository) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.historyRepository = historyRepository;
    }

    public List<Session> getAllSessions() {
        LocalDateTime now = LocalDateTime.now();
        return sessionRepository.findAll().stream()
                .filter(session -> {
                    LocalDateTime sessionDateTime = session.getDate().atTime(session.getTime());
                    return sessionDateTime.isAfter(now);
                })
                .sorted(Comparator.comparing(Session::getDate)
                        .thenComparing(Session::getTime))
                .collect(Collectors.toList());
    }

    public Session getSessionById(Long id) {
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isEmpty()) {
            throw new IllegalStateException("Session with id " + id + " does not exist");
        }
        return session.get();
    }

    public List<Session> getSessionByGenre(String genre) {
        List<Session> sessions = sessionRepository.findAll();
        List<Movie> movies = movieRepository.findAll();
        return sessions.stream()
                .filter(session -> movies.stream()
                        .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getGenre().equals(genre)))
                .sorted(Comparator.comparing(Session::getDate)
                        .thenComparing(Session::getTime))
                .collect(Collectors.toList());
    }

    public List<Session> getSessionByLanguage(String language) {
        List<Session> sessions = sessionRepository.findAll();
        List<Movie> movies = movieRepository.findAll();
        return sessions.stream()
                .filter(session -> movies.stream()
                        .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getLanguage().equals(language)))
                .sorted(Comparator.comparing(Session::getDate)
                        .thenComparing(Session::getTime))
                .collect(Collectors.toList());
    }

    public List<Session> getSessionByAgeRestriction(String ageRestriction) {
        List<Session> sessions = sessionRepository.findAll();
        List<Movie> movies = movieRepository.findAll();
        return sessions.stream()
                .filter(session -> movies.stream()
                        .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getAgeRestriction().equals(ageRestriction)))
                .sorted(Comparator.comparing(Session::getDate)
                        .thenComparing(Session::getTime))
                .collect(Collectors.toList());
    }

    public List<Session> getSessionByTime(String time) {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream()
                .filter(session -> session.getTime().toString().equals(time))
                .sorted(Comparator.comparing(Session::getDate)
                        .thenComparing(Session::getTime))
                .collect(Collectors.toList());
    }

    public List<Session> getSessionByHistory(Long clientId) {
        List<History> clientHistory = historyRepository.findAllByClientId(clientId);
        List<Session> allSessions = sessionRepository.findAll();
        List<Session> sessions = getAllSessions();
        if (clientHistory.isEmpty()) {
            return sessions;
        } else {

            // Create a map of session IDs to movie IDs
            Map<Long, Long> sessionToMovieMap = allSessions.stream()
                    .collect(Collectors.toMap(Session::getId, Session::getMovieId));
            System.out.println(sessionToMovieMap);
            // Create a map of movie IDs to genres
            Map<Long, String> movieIdToGenreMap = movieRepository.findAll().stream()
                    .collect(Collectors.toMap(Movie::getId, Movie::getGenre));
            System.out.println(movieIdToGenreMap);
            // Calculate genre counts based on session history
            Map<String, Long> genreCount = clientHistory.stream()
                    .map(session -> movieIdToGenreMap.get(sessionToMovieMap.get(session.getSessionId())))
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));
            System.out.println(genreCount);
            String mostWatchedGenre = genreCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            System.out.println(mostWatchedGenre);
            Set<Long> watchedSessionIds = clientHistory.stream()
                    .map(History::getSessionId)
                    .collect(Collectors.toSet());

            allSessions = allSessions.stream()
                    .filter(session -> watchedSessionIds.contains(session.getId()))
                    .toList();

            Map<LocalTime, Long> sessionTimeCount = allSessions.stream()
                    .collect(Collectors.groupingBy(Session::getTime, Collectors.counting()));
            System.out.println(sessionTimeCount);

            long maxCount = sessionTimeCount.values().stream()
                    .max(Long::compareTo)
                    .orElse(0L);

            List<LocalTime> mostWatchedTimes = sessionTimeCount.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxCount)
                    .map(Map.Entry::getKey)
                    .toList();
            LocalTime medianTime;
            if (mostWatchedTimes.size() % 2 == 1) {
                medianTime = mostWatchedTimes.get(mostWatchedTimes.size() / 2);
            } else {
                LocalTime middle1 = mostWatchedTimes.get(mostWatchedTimes.size() / 2 - 1);
                LocalTime middle2 = mostWatchedTimes.get(mostWatchedTimes.size() / 2);
                medianTime = middle1.plusNanos(middle1.until(middle2, java.time.temporal.ChronoUnit.NANOS) / 2);
            }

            System.out.println("Median session time: " + medianTime);

            sessions.sort((session1, session2) -> {
                String genre1 = movieIdToGenreMap.get(sessionToMovieMap.get(session1.getId()));
                String genre2 = movieIdToGenreMap.get(sessionToMovieMap.get(session2.getId()));
                int genreComparison = Long.compare(genreCount.getOrDefault(genre2, 0L), genreCount.getOrDefault(genre1, 0L));

                if (genreComparison != 0) {
                    return genreComparison;
                } else {
                    // Compare time relative to most frequently visited time
                    long timeDifference1 = Math.abs(session1.getTime().toSecondOfDay() -
                            medianTime.toSecondOfDay());
                    long timeDifference2 = Math.abs(session2.getTime().toSecondOfDay() -
                            medianTime.toSecondOfDay());

                    return Long.compare(timeDifference1, timeDifference2);
                }
            });
        }
        return sessions;
    }

    public List<LocalTime> getAllStartTimes() {
        return sessionRepository.findAll().stream()
                .map(Session::getTime)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Session> getFilteredSessions(String genre, String language, String ageRestriction, String time) {
        List<Session> sessions = sessionRepository.findAll();
        List<Movie> movies = movieRepository.findAll();
        if (genre != null) {
            sessions = sessions.stream()
                    .filter(session -> movies.stream()
                            .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getGenre().equals(genre)))
                    .collect(Collectors.toList());
        }
        if (language != null) {
            sessions = sessions.stream()
                    .filter(session -> movies.stream()
                            .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getLanguage().equals(language)))
                    .collect(Collectors.toList());
        }
        if (ageRestriction != null) {
            sessions = sessions.stream()
                    .filter(session -> movies.stream()
                            .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getAgeRestriction().equals(ageRestriction)))
                    .collect(Collectors.toList());
        }
        if (time != null) {
            LocalTime selectedTime = LocalTime.parse(time);
            sessions = sessions.stream()
                    .filter(session -> !session.getTime().isBefore(selectedTime)) // Include movies that start at or after the selected time
                    .collect(Collectors.toList());
        }
        return sessions;
    }
}
