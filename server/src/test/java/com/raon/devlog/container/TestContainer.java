package com.raon.devlog.container;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainer implements BeforeAllCallback {
	private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
	private static final int REDIS_PORT = 6379;
	private GenericContainer redis;

	private static final String MYSQL_IMAGE = "mysql:8.0.40";
	private static final String MYSQL_DB_NAME = "devlog";
	private static final String MYSQL_USERNAME = "test";
	private static final String MYSQL_PASSWORD = "test";
	private MySQLContainer mysql;

	@Override
	public void beforeAll(ExtensionContext context) {
		redis = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
			.withExposedPorts(REDIS_PORT);
		redis.start();
		System.setProperty("spring.data.redis.host", redis.getHost());
		System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT
		)));

		mysql = new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE))
			.withDatabaseName(MYSQL_DB_NAME)
			.withUsername(MYSQL_USERNAME)
			.withPassword(MYSQL_PASSWORD);
		mysql.start();

		// MySQL 컨테이너의 Spring 설정 동적으로 삽입
		System.setProperty("spring.datasource.jdbc-url", mysql.getJdbcUrl());
		System.setProperty("spring.datasource.username", mysql.getUsername());
		System.setProperty("spring.datasource.password", mysql.getPassword());
		System.setProperty("spring.datasource.driver-class-name", mysql.getDriverClassName());
	}
}
