package me.unclickable.mgui.bukkit.utils;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlUtils {

    private static final Yaml YAML = new Yaml();
    private static final Gson GSON = new Gson();

    /**
     * Converts a YAML string to a JSON string.
     *
     * @param yamlContent YAML string.
     * @return JSON string.
     */
    public static String convertYamlToJson(String yamlContent) {
        // Load YAML into an object (Map or List)
        Object loadedObject = YAML.load(yamlContent);
        // Convert the object to JSON
        return GSON.toJson(loadedObject);
    }

}