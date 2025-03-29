package ru.job4j.grabber.stores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
	private static final Logger LOG = LoggerFactory.getLogger(JdbcStore.class);
	private final Connection connection;

	public JdbcStore(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void save(Post post) {
		try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO grabber.posts(\"name\") VALUES(?);")) {
			statement.setString(1, post.getTitle());
			statement.execute();
		} catch (SQLException e) {
			LOG.error("Ошибка при добавлении поста: %s".formatted(post), e);
		}
	}

	@Override
	public List<Post> getAll() {
		List<Post> result = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT id, \"name\", \"text\", link, created_at FROM grabber.posts;")) {
			try (ResultSet posts = statement.executeQuery()) {
				while (posts.next()) {
					Post post = new Post();
					post.setId(posts.getLong("id"));
					post.setTitle(posts.getString("name"));
					post.setLink(posts.getString("link"));
					post.setDescription(posts.getString("text"));
					post.setCreatedAt(posts.getLong("created_at"));
					result.add(post);
				}
			}
		} catch (SQLException e) {
			LOG.error("Ошибка при выборке всех постов", e);
		}
		return result;
	}

	@Override
	public Optional<Post> findById(Long id) {
		Post post = null;
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT id, \"name\", \"text\", link, created_at FROM grabber.posts WHERE id = ?;"
		)) {
			statement.setLong(1, id);
			ResultSet selectedPost = statement.executeQuery();
			while (selectedPost.next()) {
				post = new Post();
				post.setId(selectedPost.getLong("id"));
				post.setTitle(selectedPost.getString("name"));
			}
		} catch (SQLException e) {
			LOG.error("Ошибка при выборке поста по id: {}", id, e);
		}
		return Optional.ofNullable(post);
	}
}
