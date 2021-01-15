package studio.rrprojects.aetreus.utils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.json.JSONObject;
import studio.rrprojects.aetreus.main.Main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONUtils {
    public static JSONObject LoadFile(String fileName) {
        String filePath = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + "JSON_Tables" + File.separator + fileName;

        JSONObject mainObj;
        try {
            FileReader reader = new FileReader(new File(filePath));
            JsonValue tmp = Json.parse(reader);
            mainObj = new JSONObject(tmp.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return mainObj;
    }
}
