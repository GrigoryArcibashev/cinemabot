package userParametersRepository.jsonUserParametersRepository;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import userParametersRepository.UserParametersRepository;
import userParametersRepository.UserParameters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JSONUserParametersRepository implements UserParametersRepository {
    private final File repository;
    private final JsonObject repositoryData;

    public JSONUserParametersRepository(String pathToRepository) {
        this.repository = new File(pathToRepository);
        if (!this.repository.exists()) {
            try {
                if (!this.repository.createNewFile())
                    System.exit(3);
                uploadRepository(new JsonObject());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.repositoryData = downloadRepository();
    }

    @Override
    public void saveUserData(String userId, UserParameters userData) {
        this.repositoryData.put(userId, Parser.parseUserParametersToJsonObject(userData));
        uploadRepository(this.repositoryData);
    }

    @Override
    public UserParameters getUserData(String userId) {
        JsonObject userParametersAsJson = (JsonObject) this.repositoryData.get(userId);
//        if (userParametersAsJson == null)
//            return new UserParameters();
        return userParametersAsJson == null ? null : Parser.parseJsonObjectToUserParameters(userParametersAsJson);
    }

    private JsonObject downloadRepository() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(this.repository.getPath()));
            JsonObject data = (JsonObject) Jsoner.deserialize(reader);
            reader.close();
            return data;
        } catch (IOException | JsonException e) {
            e.printStackTrace();
            System.exit(3);
            return null;
        }
    }

    private void uploadRepository(JsonObject data) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.repository.getPath()));
            Jsoner.serialize(data, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
