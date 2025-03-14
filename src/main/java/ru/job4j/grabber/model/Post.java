package ru.job4j.grabber.model;

import java.util.Objects;

public class Post {
	private Long id;
	private String title;
	private String link;
	private String description;
	private Long createdAt;

	public Post() { }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Post post = (Post) o;
		return Objects.equals(getLink(), post.getLink());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLink());
	}

	@Override
	public String toString() {
		return "Post{id=%s, title='%s', link='%s', description='%s', createdAt=%s}"
				.formatted(id, title, link, description, createdAt);
	}
}
