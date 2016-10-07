import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * BP3 coding challenge solution
 */
public class app {
    public static void main(String[] args) {
        ArrayList<Integer> servicetasks = new ArrayList<Integer>();
        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(".//res//diagram.json"));

            JSONArray nodes = (JSONArray) obj.get("nodes");
            JSONArray edges = (JSONArray) obj.get("edges");

            for (int i = 0; i < nodes.size(); i++) {
                JSONObject node = (JSONObject) nodes.get(i);
                String type = (String) node.get("type");
                if(type.equals("ServiceTask")) {
                    int id = ((Long) node.get("id")).intValue();
                    servicetasks.add(id);
                    nodes.remove(i);
                }
            }

            for (int k = 0; k < edges.size(); k++) {
                JSONObject edge = (JSONObject) edges.get(k);
                int from = ((Long) edge.get("from")).intValue();
                int to = ((Long) edge.get("to")).intValue();

                if(servicetasks.contains(to)) {
                    for (int j = k+1; j < edges.size(); j++) {
                        JSONObject edgeTemp = (JSONObject) edges.get(j);
                        int from2 = ((Long) edgeTemp.get("from")).intValue();
                        int to2 = ((Long) edgeTemp.get("to")).intValue();
                        if(to == from2) {
                            edge.replace("to", to2);
                        }
                    }
                }

                if(servicetasks.contains(from)) {
                    edges.remove(k);
                }
            }

            JSONObject newObj= new JSONObject();

            newObj.put("nodes", nodes);
            newObj.put("edges", edges);

            FileWriter fileWriter = new FileWriter(".//res//New-Diagram.json");
            fileWriter.write(newObj.toJSONString());
            fileWriter.flush();
            fileWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
