import org.apache.commons.io.Charsets;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Terminals {
    private final String start;
    private final String end;

    private final static Map<String, Terminals> associations;
    static {
        associations = new HashMap<>();
        associations.put("Vá.2 felé",new Terminals("Vá.1","Vá.2"));
        associations.put("Vá.1->Vá.2",new Terminals("Vá.1","Vá.2"));

        associations.put("Vá.1 felé",new Terminals("Vá.2","Vá.1"));
        associations.put("Vá.2->Vá.1",new Terminals("Vá.2","Vá.1"));

        associations.put("Garázs - Vá.1",new Terminals("Garázs","Vá.1"));
        associations.put("Vá.1 - Garázs",new Terminals("Vá.1","Garázs"));

        associations.put("Garázs - Vá.2",new Terminals("Garázs","Vá.2"));
        associations.put("Vá.2 - Garázs",new Terminals("Vá.2","Garázs"));
    }

    public Terminals(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public static Terminals FindPair(String originalKey){
        String key = WinToUTF(originalKey);
        for (Map.Entry<String, Terminals> entry : associations.entrySet()) {
            System.out.println("Trying to match [" + key + "] with [" + entry.getKey() + "]");
            if(entry.getKey().equals(key)){
                System.out.println("Success!");
                return entry.getValue();
            }

        }

        return new Terminals("NoStart","NoEnd");
    }

    @Override
    public String toString() {
        return "[" +
                "s='" + start + '\'' +
                ", e='" + end + '\'' +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terminals that = (Terminals) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    private static String WinToUTF(String original){
        byte[] originalBytes = new byte[0];
        originalBytes = original.getBytes(StandardCharsets.UTF_8);

        try {
            return new String(originalBytes, "windows-1250");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
