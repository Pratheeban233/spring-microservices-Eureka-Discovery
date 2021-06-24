package com.moviecatalog.controllers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.moviecatalog.models.CatalogItem;
import com.moviecatalog.models.Movie;
import com.moviecatalog.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getMovieCatalog(@PathVariable String userId) {
		UserRating userRating = restTemplate.getForObject("http://MOVIE-RATING-SERVICE/ratings/user/" + userId, UserRating.class);
		return Objects.requireNonNull(userRating).getRatings().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movies/" + rating.getMovieId(), Movie.class);
			return new CatalogItem(Objects.requireNonNull(movie).getName(), movie.getDescription(), rating.getRating());
		}).collect(Collectors.toList());
	}
}
