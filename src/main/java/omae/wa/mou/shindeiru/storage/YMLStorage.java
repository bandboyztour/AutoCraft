package omae.wa.mou.shindeiru.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import omae.wa.mou.shindeiru.yml.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class YMLStorage {
    private final Plugin plugin;
    private final Map<String, YamlConfiguration> configs = new HashMap<>();
    private final ConfigLoader configLoader;

    public YMLStorage(Plugin plugin) {
        this.plugin = plugin;
        configLoader = new ConfigLoader(plugin);
    }

    public void addConfig(String key, String path) {
        configs.put(key, configLoader.loadYML(plugin.getDataFolder(), path));
    }

    public YamlConfiguration getConfig(String key) {
        return configs.get(key);
    }
}
