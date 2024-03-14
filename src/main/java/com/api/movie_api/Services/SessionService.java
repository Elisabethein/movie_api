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

    /**
     * Get all sessions that are scheduled for the future and sort them by date and time, and then by rating
     *
     * @return a list of sessions
     */
    public List<Session> getAllSessions() {
        LocalDateTime now = LocalDateTime.now();
        return sessionRepository.findAll().stream()
                .filter(session -> {
                    LocalDateTime sessionDateTime = session.getDate().atTime(session.getTime());
                    return sessionDateTime.isAfter(now);
                })
                .sorted(Comparator.comparing(Session::getDate)
                        .thenComparing(Session::getTime)
                        .thenComparing(session -> movieRepository.findById(session.getMovieId())
                                .map(Movie::getRating)
                                .orElse(0.0), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Session getSessionById(Long id) {
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isEmpty()) {
            throw new IllegalStateException("Session with id " + id + " does not exist");
        }
        return session.get();
    }

    /**
     * Get all sessions with a specific genre and sort them by date and time
     *
     * @param genre - genre of the movie
     * @return a list of sessions
     */
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

    /**
     * Get all sessions with a specific language and sort them by date and time
     *
     * @param language - language of the movie
     * @return a list of sessions
     */
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

    /**
     * Get all sessions with a specific age restriction and sort them by date and time
     *
     * @param ageRestriction - age restriction of the movie
     * @return a list of sessions
     */
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

    /**
     * Get all sessions that have starting time equal or later than the given time and sort them by date and time
     * It is assumed that the user wants to see sessions equal or after chosen time, not only at the exact time
     *
     * @param time - time
     * @return a list of sessions
     */
    public List<Session> getSessionByTime(String time) {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream()
                .filter(session -> session.getTime().toString().equals(time))
                .sorted(Comparator.comparing(Session::getDate)
                        .thenComparing(Session::getTime))
                .collect(Collectors.toList());
    }

    /**
     * Method to get all sessions based on the user's history
     * Uses the user's history to suggest sessions based on the most watched genre and time
     * The algorithm sorts upcoming sessions by genres and then by starting time relative to the most frequently visited time
     * If the user has no history, it returns all sessions
     * If the user has watched many genres the same amount of times, it takes the genre order by default
     * If the user has watched sessions starting at the same time the same amount of times, it takes the median time
     *
     * @param clientId - id of the client
     * @return a list of all playing sessions sorted by the user's history preferences
     */
    public List<Session> getSessionByHistory(Long clientId) {
        if (clientId == null) {
            return getAllSessions();
        }
        List<History> clientHistory = historyRepository.findAllByClientId(clientId);
        List<Session> allSessions = sessionRepository.findAll();
        List<Session> sessions = getAllSessions();
        if (clientHistory.isEmpty()) {
            return sessions;
        } else {
            // Create a map of session IDs to movie IDs
            Map<Long, Long> sessionToMovieMap = allSessions.stream()
                    .collect(Collectors.toMap(Session::getId, Session::getMovieId));
            // Create a map of movie IDs to genres
            Map<Long, String> movieIdToGenreMap = movieRepository.findAll().stream()
                    .collect(Collectors.toMap(Movie::getId, Movie::getGenre));
            // Calculate how many times each genre has been watched using movie IDs from sessions
            Map<String, Long> genreCount = clientHistory.stream()
                    .map(session -> movieIdToGenreMap.get(sessionToMovieMap.get(session.getSessionId())))
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));
            // Get the session IDs that the user has watched
            Set<Long> watchedSessionIds = clientHistory.stream()
                    .map(History::getSessionId)
                    .collect(Collectors.toSet());
            // Filter the sessions to only include the ones that the user has watched
            allSessions = allSessions.stream()
                    .filter(session -> watchedSessionIds.contains(session.getId()))
                    .toList();
            // Create a map of session times to the number of times they have been watched
            Map<LocalTime, Long> sessionTimeCount = allSessions.stream()
                    .collect(Collectors.groupingBy(Session::getTime, Collectors.counting()));
            System.out.println(sessionTimeCount);
            // Get the number of times the most frequently watched time has been watched
            long maxCount = sessionTimeCount.values().stream()
                    .max(Long::compareTo)
                    .orElse(0L);
            // Get the most frequently watched times
            List<LocalTime> mostWatchedTimes = sessionTimeCount.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxCount)
                    .map(Map.Entry::getKey)
                    .toList();

            // Calculate the median time
            LocalTime medianTime;
            if (mostWatchedTimes.size() % 2 == 1) {
                medianTime = mostWatchedTimes.get(mostWatchedTimes.size() / 2);
            } else {
                LocalTime middle1 = mostWatchedTimes.get(mostWatchedTimes.size() / 2 - 1);
                LocalTime middle2 = mostWatchedTimes.get(mostWatchedTimes.size() / 2);
                medianTime = middle1.plusNanos(middle1.until(middle2, java.time.temporal.ChronoUnit.NANOS) / 2);
            }

            // Sort the sessions by genre and time
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

                    int timeComparison = Long.compare(timeDifference1, timeDifference2);

                    // If the times are the same, sort by rating
                    if (timeComparison == 0) {
                        int rating1 = movieRepository.findById(session1.getMovieId())
                                .map(Movie::getRating)
                                .orElse(0.0).intValue();
                        int rating2 = movieRepository.findById(session2.getMovieId())
                                .map(Movie::getRating)
                                .orElse(0.0).intValue();

                        return Integer.compare(rating2, rating1);
                    } else {
                        return timeComparison;
                    }
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

    /**
     * Get all sessions that match the given filters.
     * If a filter is null, it is not applied
     *
     * @param genre          - genre of the movie
     * @param language       - language of the movie
     * @param ageRestriction - age restriction of the movie
     * @param time           - time
     * @return a list of sessions
     */
    public List<Session> getFilteredSessions(String genre, String language, String ageRestriction, String time) {
        List<Session> sessions = getAllSessions();
        List<Movie> movies = movieRepository.findAll();
        // Filter the sessions that have chosen genre
        if (genre != null) {
            sessions = sessions.stream()
                    .filter(session -> movies.stream()
                            .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getGenre().equals(genre)))
                    .collect(Collectors.toList());
        }
        // Filter the remaining sessions that have chosen language
        if (language != null) {
            sessions = sessions.stream()
                    .filter(session -> movies.stream()
                            .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getLanguage().equals(language)))
                    .collect(Collectors.toList());
        }
        // Filter the remaining sessions that have chosen age restriction
        if (ageRestriction != null) {
            sessions = sessions.stream()
                    .filter(session -> movies.stream()
                            .anyMatch(movie -> movie.getId().equals(session.getMovieId()) && movie.getAgeRestriction().equals(ageRestriction)))
                    .collect(Collectors.toList());
        }
        // Filter the remaining sessions that have starting time equal or later than the chosen time
        if (time != null) {
            LocalTime selectedTime = LocalTime.parse(time);
            sessions = sessions.stream()
                    .filter(session -> !session.getTime().isBefore(selectedTime)) // Include movies that start at or after the selected time
                    .collect(Collectors.toList());
        }
        return sessions;
    }
}
