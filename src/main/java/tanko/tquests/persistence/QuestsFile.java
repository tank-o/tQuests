package tanko.tquests.persistence;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class QuestsFile {
    private static File questsFile;
    private static FileConfiguration questsFileConfig;

    //Finds existent questsdata file or generates questsdata file
    public static void setup(JavaPlugin plugin){
        questsFile = new File(plugin.getDataFolder() + "/quests.yml");
        if (!questsFile.exists()){
            try{
                questsFile.createNewFile();
            }catch (IOException e){
                //stop in the name of plod
            }
        }
        questsFileConfig = YamlConfiguration.loadConfiguration(questsFile);
        questsFileConfig.options().copyDefaults(true);
    }

    //Get instance of file configuration
    public static FileConfiguration getFile(){
        return questsFileConfig;
    }

    //Save file
    public static void save(){
        try{
            questsFileConfig.save(questsFile);
        }catch (IOException e){
            Bukkit.getLogger().warning("couldn't save file");
        }

    }

    //Reload File
    public static void reload(){
        questsFileConfig = YamlConfiguration.loadConfiguration(questsFile);
    }
}
