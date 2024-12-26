package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.project.entity.Movie;
import com.example.project.entity.QMovie;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface MovieRepository extends JpaRepository<Movie, Long>, QuerydslPredicateExecutor<Movie> {

    @Query("SELECT g.name FROM Movie m JOIN MovieGenre mg ON m.id = mg.movie.id JOIN Genre g ON mg.genre.id = g.id WHERE m.id = :id")
    List<String> getGenre(Long id);

    @Query("SELECT p.name FROM Movie m JOIN MoviePeople mp ON m.id = mp.movie.id JOIN People p ON mp.people.id = p.id WHERE m.id = :id")
    List<String> getpeople(Long id);

    default Predicate makePredicate(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;

        // id > 0: range scan
        builder.and(qMovie.id.gt(0L));

        if (type == null)
            return builder;

        // // 카테고리
        // if (type.equals("c")) {
        // builder.and(qMovie.category.name.contains(keyword));
        // }
        // // 제목
        // else if (type.equals("t")) {
        // builder.and(qMovie.title.contains(keyword));
        // }
        // // 저자
        // else if (type.equals("w")) {
        // builder.and(qMovie.writer.contains(keyword));
        // }
        if (type.equals("t")) {
            builder.and(qMovie.title.contains(keyword));
        }

        return builder;
    }

}
