package com.example.project.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.entity.Genre;
import com.example.project.entity.Movie;

public interface AdminMovieRepository extends JpaRepository<Movie, Long> {

        @Query("SELECT m, g, mg FROM MovieGenre mg " +
                        "JOIN mg.id.genre g " +
                        "JOIN mg.id.movie m " +
                        "WHERE m.id = :id")
        List<Object[]> getMovieDetailsWithGenres(Long id);

        @Query(value = """
                        SELECT
                        m.id,
                        m.TITLE AS movie_title,
                        LISTAGG(DISTINCT g.name, ', ') WITHIN GROUP (ORDER BY g.id) AS genres,
                        m.RELEASE_DATE AS release_date
                        FROM
                        Movies m
                        JOIN
                        MOVIE_GENRE mg ON m.ID = mg.id.movie.id
                        JOIN
                        Genres g ON g.ID = mg.id.genre.id
                        GROUP BY
                        m.id,m.TITLE, m.RELEASE_DATE, m.ID
                        ORDER BY
                        m.RELEASE_DATE DESC
                        """, nativeQuery = true)
        List<Object[]> getMovieDetails();

        @Query(value = """
                                    SELECT
                                PPPP.id,
                        	PPPP.TITLE,
                        	LISTAGG(PPPP.name, ', ') WITHIN GROUP (ORDER BY PPPP.name) AS people
                        FROM
                        	(
                        	SELECT
                        		m.id,
                        		m.title,
                        		p.popularity,
                        		p.name,
                        		ROW_NUMBER() OVER (PARTITION BY m.id ORDER BY p.popularity DESC) AS rnk
                        	FROM
                        		Movies m
                        	LEFT JOIN
                        	MOVIE_PERSON mp
                                 ON
                        		m.ID = mp.MOVIE_ID
                        	JOIN
                                PEOPLE p ON
                        		p.ID = mp.PERSON_ID
                        	WHERE
                        		p.job = 'Acting') PPPP
                        WHERE
                        	rnk <= 3
                        GROUP BY
                                PPPP.id,PPPP.TITLE
                                    """, nativeQuery = true)
        List<Object[]> movieActers();

        @Query(value = """
                                            SELECT
                                PPPP.id,
                        	PPPP.TITLE,
                        	LISTAGG(PPPP.name, ', ') WITHIN GROUP (ORDER BY PPPP.name) AS people
                        FROM
                        	(
                        	SELECT
                        		m.id,
                        		m.title,
                        		p.popularity,
                        		p.name,
                        		ROW_NUMBER() OVER (PARTITION BY m.id ORDER BY p.popularity DESC) AS rnk
                        	FROM
                        		Movies m
                        	LEFT JOIN
                        	MOVIE_PERSON mp
                                 ON
                        		m.ID = mp.MOVIE_ID
                        	JOIN
                                PEOPLE p ON
                        		p.ID = mp.PERSON_ID
                        	WHERE
                        		p.job = 'Directing') PPPP
                        WHERE
                        	rnk <= 1
                        GROUP BY
                                PPPP.id,PPPP.TITLE
                                            """, nativeQuery = true)
        List<Object[]> movieDirector();

        @Query(value = """
                            SELECT
                                m.id,
                                m.TITLE AS movie_title,
                                LISTAGG(g.name, ', ') WITHIN GROUP (ORDER BY g.name) AS genres,
                                m.RELEASE_DATE AS release_date
                            FROM Movies m
                            JOIN MOVIE_GENRE mg ON m.ID = mg.MOVIE_ID
                            JOIN Genre g ON g.ID = mg.GENRE_ID
                            GROUP BY m.id, m.TITLE, m.RELEASE_DATE
                        """, nativeQuery = true)
        Page<Object[]> getMovieDetails(Pageable pageable);

}
