package omae.wa.mou.shindeiru.storage;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageStorage {
    private final Map<String, String> messages = new HashMap<>();
    private final YMLStorage ymlStorage;

    public MessageStorage(YMLStorage ymlStorage) {
        this.ymlStorage = ymlStorage;
        loadMessages();
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    private void loadMessages() {
        YamlConfiguration messages = ymlStorage.getConfig("messages");
        Set<String> keys = messages.getKeys(false);

        for (String key : keys) {
            this.messages.put(key, messages.getString(key).replace("&", "§"));
        }
    }
}
