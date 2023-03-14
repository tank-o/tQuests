package tanko.tquests.persistence;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ProgressFile {
    private static File playerProgressFile;
    private static FileConfiguration playerProgressFileConfig;

    //Finds existent playerProgressdata file or generates playerProgressdata file
    public static void setup(JavaPlugin plugin){
        playerProgressFile = new File(plugin.getDataFolder() + "/playerProgress.yml");
        if (!playerProgressFile.exists()){
            try{
                playerProgressFile.createNewFile();
            }catch (IOException e){
                //stop in the name of plod
            }
        }
        playerProgressFileConfig = YamlConfiguration.loadConfiguration(playerProgressFile);
        playerProgressFileConfig.options().copyDefaults(true);
    }

    //Get instance of file configuration
    public static FileConfiguration getFile(){
        return playerProgressFileConfig;
    }

    //Save file
    public static void save(){
        try{
            playerProgressFileConfig.save(playerProgressFile);
        }catch (IOException e){
            Bukkit.getLogger().warning("couldn't save file");
        }

    }

    //Reload File
    public static void reload(){
        playerProgressFileConfig = YamlConfiguration.loadConfiguration(playerProgressFile);
    }
}
