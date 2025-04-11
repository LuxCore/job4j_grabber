package ru.job4j.grabber.utils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HabrCareerDateTimeParserTest {

	@org.junit.jupiter.api.Test
	void testParseDateTime() {
		String date = "2025-04-11T07:56:30+03:00";
		LocalDateTime ldt = new HabrCareerDateTimeParser().parse(date);
		assertThat(ldt).isEqualTo(LocalDateTime.of(2025, 4, 11, 7, 56, 30));
	}
}