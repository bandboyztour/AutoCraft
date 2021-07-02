package omae.wa.mou.shindeiru;

import omae.wa.mou.shindeiru.database.SQLite;
import omae.wa.mou.shindeiru.listener.AutoCraftListener;
import omae.wa.mou.shindeiru.manager.AutoCraftManager;
import omae.wa.mou.shindeiru.storage.MessageStorage;
import omae.wa.mou.shindeiru.storage.YMLStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import omae.wa.mou.shindeiru.database.DataBase;

@Plugin(name = "AutoCraft", version = "1.0.0")
public class AutoCraftPlugin extends JavaPlugin {
    private final YMLStorage ymlStorage = new YMLStorage(this);

    private DataBase dataBase;

    @Override
    public void onEnable() {
        loadConfigs();

        dataBase = new SQLite(getDataFolder().getPath(), "autocraft.db");
        dataBase.connect();

        AutoCraftManager autoCraftManager = new AutoCraftManager(this, dataBase);
        autoCraftManager.start();

        MessageStorage messageStorage = new MessageStorage(ymlStorage);

        Bukkit.getPluginManager().registerEvents(new AutoCraftListener(autoCraftManager, ymlStorage, messageStorage, dataBase), this);
    }

    @Override
    public void onDisable() {
        dataBase.disconnect();
    }

    private void loadConfigs() {
        ymlStorage.addConfig("config", "config.yml");
        String language = ymlStorage.getConfig("config").getString("language");
        ymlStorage.addConfig("messages", language + "_messages.yml");
    }
}
