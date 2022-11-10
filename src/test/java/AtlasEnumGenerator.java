import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

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
        TextureAtlas.AtlasRegion region;
        OrderedMap<String, Integer> indexCounts = new OrderedMap<>(regions.size);
        for (int i = 0; i < regions.size; i++) {
            region = regions.get(i);
            indexCounts.put(region.name, Math.max(indexCounts.get(region.name, -1), region.index));
        }
        StringBuilder sb = new StringBuilder(indexCounts.size * 16);
        sb.append("import com.badlogic.gdx.graphics.g2d.TextureAtlas;\n")
                .append("import com.badlogic.gdx.utils.Array;\n")
                .append("import java.util.HashMap;\n\n");
        sb.append("public enum Dawnlike {\n");
        for (ObjectMap.Entry<String, Integer> e : indexCounts.entries())
        {
            sb.append('\t').append(nameEnum(e.key)).append(",\n");
//                    .append("(\"").append(e.getKey()).append("\"),\n");
//                    .append("\", ").append(e.getValue() + 1 | e.getValue() >> 31).append("),\n");
        }
//        Scaling.fillX.name().toLowerCase().replace('_', ' ');
        sb.setCharAt(sb.length() - 2, ';');
        sb
//                .append("\n\tpublic final String name;\n")
////                .append("\tpublic final int indices;\n")
//                .append("\tDawnlike(final String name) {\n")
////                .append("\tDawnlike(String name, int indices) {\n")
//                .append("\t\tthis.name = name;\n")
////                .append("\t\tthis.indices = indices;\n")
//                .append("\t}\n")
                .append("\n\tpublic static HashMap<Dawnlike, Array<TextureAtlas.AtlasRegion>> mapping(TextureAtlas atlas) {\n")
                .append("\t\tfinal Dawnlike[] enums = values();\n")
                .append("\t\tfinal HashMap<Dawnlike, Array<TextureAtlas.AtlasRegion>> hm = new HashMap<Dawnlike, Array<TextureAtlas.AtlasRegion>>(enums.length);\n")
                .append("\t\tfor(Dawnlike e : enums) {\n")
                .append("\t\t\thm.put(e, atlas.findRegions(e.name().toLowerCase().replace('_', ' ')));\n")
                .append("\t\t}\n")
                .append("\t\treturn hm;\n")
                .append("\t}\n")
                .append("}\n");

        System.out.println(sb);
    }
    public static OrderedMap<String, Array<TextureAtlas.AtlasRegion>> mapping(final TextureAtlas atlas){
        final Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        TextureAtlas.AtlasRegion item;
        final OrderedMap<String, Array<TextureAtlas.AtlasRegion>> lhm = new OrderedMap<String, Array<TextureAtlas.AtlasRegion>>(regions.size, 0.5f);
        for (int i = 0; i < regions.size; i++) {
            if(!lhm.containsKey((item = regions.get(i)).name))
                lhm.put(item.name, atlas.findRegions(item.name));
        }
        return lhm;
    }
    public static OrderedMap<String, Animation<TextureAtlas.AtlasRegion>> animationMapping(final TextureAtlas atlas, final float frameTime){
        final Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        TextureAtlas.AtlasRegion item;
        final OrderedMap<String, Animation<TextureAtlas.AtlasRegion>> lhm = new OrderedMap<>(regions.size, 0.5f);
        for (int i = 0; i < regions.size; i++) {
            if(!lhm.containsKey((item = regions.get(i)).name))
                lhm.put(item.name, new Animation<>(frameTime, atlas.findRegions(item.name), Animation.PlayMode.LOOP));
        }
        return lhm;
    }
}
