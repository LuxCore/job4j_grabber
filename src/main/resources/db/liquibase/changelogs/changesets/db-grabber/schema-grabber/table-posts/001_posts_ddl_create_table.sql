CREATE TABLE grabber.posts(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	"name" TEXT,
	"text" TEXT,
	link TEXT,
	created_at TIMESTAMP,
	CONSTRAINT posts_pk PRIMARY KEY(id)
);

CREATE UNIQUE INDEX posts_name_uidx ON grabber.posts(link);