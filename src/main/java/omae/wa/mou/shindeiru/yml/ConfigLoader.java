package omae.wa.mou.shindeiru.yml;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    private final Plugin plugin;

    public ConfigLoader(Plugin plugin) {
        this.plugin = plugin;
    }

    public YamlConfiguration loadYML(File path, String fileName) {
        File file = new File(path, fileName);
        YamlConfiguration yamlConfiguration = null;

        if (!file.exists()) {
            path.getParentFile().mkdir();
            plugin.saveResource(fileName, false);
        }

        try {
            yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }

        return yamlConfiguration;
    }
}
