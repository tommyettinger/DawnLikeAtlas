import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tommy Ettinger on 9/14/2019.
 */
public class AtlasEnumGenerator extends ApplicationAdapter {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("AtlasEnumGenerator");
        config.setWindowedMode(500, 100);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        final AtlasEnumGenerator app = new AtlasEnumGenerator();
        new Lwjgl3Application(app, config);
    }
    
    public static String nameEnum(String baseName)
    {
        return baseName.toUpperCase().replace(' ', '_');
    }
    
    @Override
    public void create() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.local("atlas/Dawnlike.atlas"));
        //TextureAtlas.TextureAtlasData atlasData = new TextureAtlas.TextureAtlasData(Gdx.files.local("atlas/Dawnlike.atlas"), Gdx.files.local("atlas"), false);
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        TreeMap<String, Integer> indexCounts = new TreeMap<>();
        TextureAtlas.AtlasRegion region;
        for (int i = 0; i < regions.size; i++) {
            region = regions.get(i);
            indexCounts.put(region.name, Math.max(indexCounts.getOrDefault(region.name, -1), region.index));
        }
        StringBuilder sb = new StringBuilder(indexCounts.size() * 16);
        sb.append("import com.badlogic.gdx.graphics.g2d.TextureAtlas;\n")
                .append("import com.badlogic.gdx.utils.Array;\n")
                .append("import java.util.HashMap;\n\n");
        sb.append("public enum Dawnlike {\n");
        for (Map.Entry<String, Integer> e : indexCounts.entrySet())
        {
            sb.append('\t').append(nameEnum(e.getKey())).append("(\"").append(e.getKey())
                    .append("\", ").append(e.getValue() + 1 | e.getValue() >> 31).append("),\n");
        }
        sb.setCharAt(sb.length() - 2, ';');
        sb
                .append("\n\tpublic final String name;\n")
                .append("\tpublic final int indices;\n")
                .append("\tprotected Dawnlike(String name, int indices) {\n")
                .append("\t\tthis.name = name;\n")
                .append("\t\tthis.indices = indices;\n")
                .append("\t}\n")
                .append("\tpublic static HashMap<Dawnlike, Array<TextureAtlas.AtlasRegion>> mapping(TextureAtlas atlas) {\n")
                .append("\t\tfinal Dawnlike[] enums = values();\n")
                .append("\t\tfinal HashMap<Dawnlike, Array<TextureAtlas.AtlasRegion>> hm = new HashMap<Dawnlike, Array<TextureAtlas.AtlasRegion>>(enums.length);\n")
                .append("\t\tfor(Dawnlike e : enums) {\n")
                .append("\t\t\thm.put(e, atlas.findRegions(e.name));\n")
                .append("\t\t}\n")
                .append("\t\treturn hm;\n")
                .append("\t}\n")
                .append("}\n");

        System.out.println(sb);
    }
}
