package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.ScheduleManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.stores.Store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		Config config = new Config();
		config.load("src/main/resources/application.properties");
		try {
			Class.forName(config.get("db.driver-class-name"));
			Connection connection = DriverManager.getConnection(
					config.get("db.url"),
					config.get("db.username"),
					config.get("db.password")
			);
			Store store = new JdbcStore(connection);
			Post post = new Post();
			post.setTitle("Super Java Job");
			store.save(post);
			ScheduleManager scheduler = new ScheduleManager();
			scheduler.init();
			scheduler.load(Integer.parseInt(config.get("rabbit.interval")), SuperJobGrab.class, store);
		} catch (ClassNotFoundException e) {
			LOG.error("Ошибка при загрузке драйвера БД", e);
		} catch (SQLException e) {
			LOG.error("Ошибка при подключении к БД", e);
		}
	}
}
