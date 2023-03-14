package tanko.tquests.persistence;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class QuestlinesFile {
    private static File questlinesFile;
    private static FileConfiguration questlinesFileConfig;

    //Finds existent questlinesdata file or generates questlinesdata file
    public static void setup(JavaPlugin plugin){
        questlinesFile = new File(plugin.getDataFolder() + "/questlines.yml");
        if (!questlinesFile.exists()){
            try{
                questlinesFile.createNewFile();
            }catch (IOException e){
                //stop in the name of plod
            }
        }
        questlinesFileConfig = YamlConfiguration.loadConfiguration(questlinesFile);
        questlinesFileConfig.options().copyDefaults(true);
    }

    //Get instance of file configuration
    public static FileConfiguration getFile(){
        return questlinesFileConfig;
    }

    //Save file
    public static void save(){
        try{
            questlinesFileConfig.save(questlinesFile);
        }catch (IOException e){
            Bukkit.getLogger().warning("couldn't save file");
        }

    }

    //Reload File
    public static void reload(){
        questlinesFileConfig = YamlConfiguration.loadConfiguration(questlinesFile);
    }
}
