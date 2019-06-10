package edu.uci.ics.perpetual.persistence;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

public class RedisConnectionManager {
    private static final int PORT_NUM = 6379;
    private static RedisConnection<String, String> connection = null;
    private static RedisClient redisClient = null;

    // One connection to one host (localhost) should be enough for now
    // We can add a connection pool later if necessary
    public static RedisConnection<String, String> getConnection() {
        if (connection == null) {
            redisClient = new RedisClient(
                    RedisURI.create("redis://localhost:" + PORT_NUM));
            connection = redisClient.connect();

            System.out.println("Connected to Redis");
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null && redisClient != null) {
            connection.close();
            redisClient.shutdown();

            System.out.println("Closed Redis connection");
        }
    }

    public static void main(String args[]) {
        System.out.println("hello");
        getConnection();
        closeConnection();
    }
}
