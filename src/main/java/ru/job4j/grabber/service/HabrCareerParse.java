package ru.job4j.grabber.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.model.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
	private static final Logger LOG = LoggerFactory.getLogger(HabrCareerParse.class);
	private static final String SOURCE_LINK = "https://career.habr.com";
	private static final String PREFIX = "/vacancies?page=";
	private static final String SUFFIX = "&q=Java%20developer&type=all";
	private static final byte PARSE_PAGE_COUNT = 5;

	@Override
	public List<Post> fetch() {
		List<Post> result = new ArrayList<>();
		byte pageNumber = 1;
		for (byte i = pageNumber; i <= PARSE_PAGE_COUNT; i++) {
			String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
			try {
				Connection connection = Jsoup.connect(fullLink);
				Document document = connection.get();
				Elements elements = document.select(".vacancy-card__inner");
				elements.forEach(element -> {
					Element title = element.select(".vacancy-card__title").first();
					if (title != null) {
						Element link = title.child(0);
						String vacancyName = title.text();
						String vacancyLink = "%s%s".formatted(SOURCE_LINK, link.attr("href"));
						Element timestamp = element.select(".vacancy-card__date").first().child(0);
						String ts = timestamp.attr("datetime");
						LocalDateTime localTimestamp = ZonedDateTime.parse(ts,
										DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").withZone(ZoneOffset.of("+03:00")))
								.toLocalDateTime();
						Long vacancyCreatedAt = localTimestamp.atOffset(ZoneOffset.of("+03:00")).toEpochSecond();
						Post post = new Post();
						post.setTitle(vacancyName);
						post.setLink(vacancyLink);
						post.setCreatedAt(vacancyCreatedAt);
						result.add(post);
					}
				});
			} catch (IOException e) {
				LOG.error("Ошибка при получении страницы {}", fullLink, e);
			}
		}
		return result;
	}
}
