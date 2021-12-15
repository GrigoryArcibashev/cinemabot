package userParametersRepository.dataBase;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RedisDataBase implements DataBase{
    private final Jedis dataBase;
    private String host;
    private Integer port;

    public RedisDataBase(){
        uploadHostAndPort();
        this.dataBase = new Jedis(host, port);
    }

    @Override
    public void Set(String key, String value) {
        dataBase.set(key, value);
    }

    @Override
    public String Get(String key) {
        return dataBase.get(key);
    }

    private void uploadHostAndPort() {
        try {
            String fullPath = "../cinemabot/config.json";
            Reader reader = Files.newBufferedReader(Paths.get(fullPath));
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            this.host = (String) parser.get("redis-host");
            this.port = ((BigDecimal) parser.get("redis-port")).intValueExact();
        } catch (IOException | JsonException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
