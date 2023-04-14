import java.util.HashMap;
import java.util.Map;

public class Stations {
    private static Map<String, StationPair> associations;
    static {
        associations = new HashMap<>();
        associations.put("Vá.2 felé",new StationPair("Vá.1","Vá.2"));
        associations.put("Vá.1->Vá.2",new StationPair("Vá.1","Vá.2"));

        associations.put("Vá.1 felé",new StationPair("Vá.2","Vá.1"));
        associations.put("Vá.2->Vá.1",new StationPair("Vá.2","Vá.1"));

        associations.put("Garázs - Vá.1",new StationPair("Garázs","Vá.1"));
        associations.put("Vá.1 - Garázs",new StationPair("Vá.1","Garázs"));

        associations.put("Garázs - Vá.2",new StationPair("Garázs","Vá.2"));
        associations.put("Vá.2 - Garázs",new StationPair("Vá.2","Garázs"));
    }

    public static StationPair FindPair(String key){
        for (Map.Entry entry : associations.entrySet()) {
            if(entry.getKey().equals(key)){
                return (StationPair) entry.getValue();
            }
        }

        return new StationPair("NoStart","NoEnd");
    }
}
