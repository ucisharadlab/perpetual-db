package edu.uci.ics.perpetual.persistence;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

public class RedisConnectionManager {

    private RedisConnection connection;

    void getConnection() {
        RedisClient redisClient = new RedisClient(
                RedisURI.create("redis://localhost:port"));
        RedisConnection<String, String> connection = redisClient.connect();

        System.out.println("Connected to Redis");

        connection.close();
        redisClient.shutdown();
    }

}
