package ru.job4j.grabber.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private static final Logger LOG = LoggerFactory.getLogger(Config.class);
	private final Properties properties = new Properties();

	public void load(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			properties.load(in);
		} catch (FileNotFoundException e) {
			LOG.error("Файл не найден", e);
		} catch (IOException e) {
			LOG.error("Ошибка при загрузке файла %s".formatted(file), e);
		}
	}

	public String get(String key) {
		return properties.getProperty(key);
	}
}
