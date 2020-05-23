import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import dawnliker.AnimatedGif;
import dawnliker.Coloring;
import dawnliker.PNG8;
import dawnliker.PaletteReducer;

import java.io.IOException;

public class Splitter extends ApplicationAdapter {
    //public static final int backgroundColor = Color.rgba8888(Color.DARK_GRAY);
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
//    protected SpriteBatch batch;
//    protected Viewport screenView;
//    protected Texture screenTexture;
//    protected BitmapFont font;
    protected PaletteReducer reducer;
    protected PNG8 png8;
    protected AnimatedGif gif;

    public static final String[] listing = {
            "Ammo",
            "Amulet",
            "Aquatic0",
            "Armor",
            "Avian0",
            "Book",
            "Boot",
            "Cat0",
            "Chest_Closed",
            "Chest_Open",
            "Decor",
            "Demon0",
            "Dog0",
            "Door_Closed",
            "Door_Open",
            "Effect0",
            "Elemental0",
            "Engineer",
            "Fence",
            "Flesh",
            "Floor",
            "Food",
            "GUI",
            "Glove",
            "Ground",
            "Hat",
            "Hill",
            "Humanoid0",
            "Icons",
            "Key",
            "Light",
            "Logo",
            "LongWep",
            "Mage",
            "Map",
            "MedWep",
            "Misc0",
            "Money",
            "Music",
            "Ore0",
            "Paladin",
            "Pest0",
            "Pit",
            "Plant0",
            "Player0",
            "Potion",
            "Quadruped0",
            "Reptile0",
            "Ring",
            "Rock",
            "Rodent0",
            "Rogue",
            "Scroll",
            "Shield",
            "ShortWep",
            "Slime0",
            "Template",
            "Tile",
            "Tool",
            "Trap",
            "Tree",
            "Undead0",
            "Wall",
            "Wand",
            "Warrior",

//            "Dungeon"
    };
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Palette Reducer");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        final Splitter app = new Splitter();
        new Lwjgl3Application(app, config);
    }

    public void split() {
        Gdx.files.local("renamed/").mkdirs();
        for(String name : listing) {
            String contents = Gdx.files.internal(name + ".txt").readString();
            String[] lines = contents.split("\\R");
            if(name.endsWith("0"))
            {
                String name2 = name.substring(0, name.length()-1);
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j]))
                        {
                            Gdx.files.local("individual/"+name2+"_"+j+"x"+i+"_0.png").copyTo(Gdx.files.local("renamed/"+cell[j]+"_0.png"));
                            Gdx.files.local("individual/"+name2+"_"+j+"x"+i+"_1.png").copyTo(Gdx.files.local("renamed/"+cell[j]+"_1.png"));
                        }
                    }
                }
            }
            else if(name.endsWith("_Closed") || name.endsWith("_Open"))
            {
                boolean open = name.endsWith("_Open");
                String name2 = name.substring(0, name.lastIndexOf('_'));
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j]))
                        {
                            Gdx.files.local("individual/"+name2+"_"+j+"x"+i+"_"+(open ? "1" : "0")+".png").copyTo(Gdx.files.local("renamed/"+cell[j]+".png"));
                        }
                    }
                }
            }
            else if("Decor".equals(name) || "GUI".equals(name) || "Ground".equals(name) || "Hill".equals(name) || "Map".equals(name)
                    || "Pit".equals(name) || "Trap".equals(name) || "Tree".equals(name))
            {
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j])) {
                            if (cell[j].endsWith("_0")) {
                                Gdx.files.local("individual/" + name + "_" + j + "x" + i + "_0" + ".png").copyTo(Gdx.files.local("renamed/" + cell[j] + ".png"));
                                Gdx.files.local("individual/" + name + "_" + j + "x" + i + "_1" + ".png").copyTo(Gdx.files.local("renamed/" + cell[j].substring(0, cell[j].length() - 2) + "_1.png"));
                            }
                            else
                                Gdx.files.local("individual/"+name+"_"+j+"x"+i+"_0.png").copyTo(Gdx.files.local("renamed/"+cell[j]+".png"));
                        }
                    }
                }
            }
//            else if("Dungeon".equals(name))
//            {
//                for (int i = 0; i < lines.length; i++) {
//                    String[] cell = lines[i].split("\t");
//                    for (int j = 0; j < cell.length; j++) {
//                        if(! "".equals(cell[j]))
//                        {
//                            Gdx.files.local("altIndividual/"+name+"_"+j+"x"+i+".png").moveTo(Gdx.files.local("altIndividual/"+cell[j]+".png"));
//                        }
//                    }
//                }
//            }
            else
            {
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j]))
                        {
                            Gdx.files.local("individual/"+name+"_"+j+"x"+i+".png").copyTo(Gdx.files.local("renamed/"+cell[j]+".png"));
                        }
                    }
                }
            }
        }

    }

    public void animate() {
        Gdx.files.local("docs/animated/").mkdirs();
        Array<Pixmap> arr = new Array<>(4);
        StringBuilder sb = new StringBuilder(65536);
        sb.append(
                "<html>\n" 
                        + "<head>\n" 
                        + "<title>Dawnlike Atlas Preview!</title>\n" 
                        + "<style>" +
                        "img{\n" +
                        "-ms-interpolation-mode: nearest-neighbor;\n" +
                        "    image-rendering: -webkit-optimize-contrast;\n" +
                        "    image-rendering: -moz-crisp-edges;\n" +
                        "    image-rendering: -o-pixelated;\n" +
                        "    image-rendering: pixelated;\n" +
                        "    width: 32px;\n" +
                        "    height: 32px;\n" +
                        "}</style>\n" 
                        + "</head>\n" 
                        + "<body>\n"
           + "<p>This is a listing of the tiles in the <a href=\"https://opengameart.org/content/dawnlike-16x16-universal-rogue-like-tileset-v181\">DawnLike tileset by DragonDePlatino and DawnBringer</a>.\n"
           + "The specific variant used is <a href=\"https://github.com/tommyettinger/DawnLikeAtlas\">DawnLikeAtlas</a>, which splits up DawnLike into these individual tiles for usage by libGDX in a TextureAtlas," 
           + "or by other engines/frameworks that work better when accessing tiles by name.\n"
           + "The tileset is CC-BY-4.0 licensed, requiring attribution to DragonDePlatino and DawnBringer. The code is Apache 2.0 licensed.</p>\n" + "<br>\n");
        for(String name : listing) {
            String contents = Gdx.files.internal(name + ".txt").readString();
            String[] lines = contents.split("\\R");
            if(name.endsWith("0"))
            {
                String name2 = name.substring(0, name.length()-1);
                sb.append("<h2>").append(name2).append("</h2>\n");
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    sb.append("<p>");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j]))
                        {
                            arr.clear();
                            arr.add(new Pixmap(Gdx.files.local("renamed/"+cell[j]+"_0.png")));
                            arr.add(new Pixmap(Gdx.files.local("renamed/"+cell[j]+"_1.png")));
                            try {
                                gif.write(Gdx.files.local("docs/animated/"+cell[j]+".gif"), arr, 2);
                            } catch (IOException e) {
                                System.err.println("HAD A PROBLEM IN GROUP " + name2 + " WITH " + cell[j]);
                            }
                            sb.append(cell[j]).append(": <img src=\"animated/").append(cell[j]).append(".gif\" />\n");
                        }
                    }
                    sb.append("</p>\n");
                }
            }
            else if(name.endsWith("_Closed") || name.endsWith("_Open"))
            {
                boolean open = name.endsWith("_Open");
                String name2 = name.substring(0, name.lastIndexOf('_'));
                if(!open)
                    sb.append("<h2>").append(name2).append("</h2>\n");
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    sb.append("<p>");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j]))
                        {
                            if(cell[j].endsWith("_0")) {
                                arr.clear();
                                arr.add(new Pixmap(Gdx.files.local("renamed/" + cell[j] + ".png")));
                                arr.add(new Pixmap(Gdx.files.local("renamed/" + cell[j].substring(0, cell[j].length() - 2) + "_1.png")));
                                try {
                                    gif.write(Gdx.files.local("docs/animated/" + cell[j].substring(0, cell[j].length() - 2) + ".gif"), arr, 2);
                                } catch (IOException e) {
                                    System.err.println("HAD A PROBLEM IN GROUP " + name2 + " WITH " + cell[j]);
                                }
                                sb.append(cell[j], 0, cell[j].length() - 2).append(": <img src=\"animated/").append(cell[j], 0, cell[j].length() - 2).append(".gif\" />\n");
                            }
                            else if(!cell[j].endsWith("_1"))
                            {
                                Gdx.files.local("renamed/"+cell[j]+".png").copyTo(Gdx.files.local("docs/animated/"+cell[j]+".png"));
                                sb.append(cell[j]).append(": <img src=\"animated/").append(cell[j]).append(".png\" />\n");
                            }
                        }
                    }
                    sb.append("</p>\n");
                }
            }
            else if("Decor".equals(name) || "GUI".equals(name) || "Ground".equals(name) || "Hill".equals(name) || "Map".equals(name)
                    || "Pit".equals(name) || "Trap".equals(name) || "Tree".equals(name))
            {
                sb.append("<h2>").append(name).append("</h2>\n");
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    sb.append("<p>");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j])) {
                            if (cell[j].endsWith("_0")) {
                                Gdx.files.local("individual/" + name + "_" + j + "x" + i + "_0" + ".png").copyTo(Gdx.files.local("renamed/" + cell[j] + ".png"));
                                Gdx.files.local("individual/" + name + "_" + j + "x" + i + "_1" + ".png").copyTo(Gdx.files.local("renamed/" + cell[j].substring(0, cell[j].length() - 2) + "_1.png"));

                                arr.clear();
                                arr.add(new Pixmap(Gdx.files.local("renamed/"+cell[j]+".png")));
                                arr.add(new Pixmap(Gdx.files.local("renamed/" + cell[j].substring(0, cell[j].length() - 2) + "_1.png")));
                                try {
                                    gif.write(Gdx.files.local("docs/animated/"+cell[j].substring(0, cell[j].length() - 2)+".gif"), arr, 2);
                                } catch (IOException e) {
                                    System.err.println("HAD A PROBLEM IN GROUP " + name + " WITH " + cell[j]);
                                }
                                sb.append(cell[j], 0, cell[j].length() - 2).append(": <img src=\"animated/").append(cell[j], 0, cell[j].length() - 2).append(".gif\" />\n");
                            }
                            else
                            {
                                Gdx.files.local("renamed/"+cell[j]+".png").copyTo(Gdx.files.local("docs/animated/"+cell[j]+".png"));
                                sb.append(cell[j]).append(": <img src=\"animated/").append(cell[j]).append(".png\" />\n");
                            } 
                        }
                    }
                    sb.append("</p>\n");
                }
            }
//            else if("Dungeon".equals(name))
//            {
//                for (int i = 0; i < lines.length; i++) {
//                    String[] cell = lines[i].split("\t");
//                    for (int j = 0; j < cell.length; j++) {
//                        if(! "".equals(cell[j]))
//                        {
//                            Gdx.files.local("altIndividual/"+name+"_"+j+"x"+i+".png").moveTo(Gdx.files.local("altIndividual/"+cell[j]+".png"));
//                        }
//                    }
//                }
//            }
            else
            {
                sb.append("<h2>").append(name).append("</h2>\n");
                for (int i = 0; i < lines.length; i++) {
                    String[] cell = lines[i].split("\t");
                    sb.append("<p>");
                    for (int j = 0; j < cell.length; j++) {
                        if(! "".equals(cell[j]))
                        {
                            Gdx.files.local("renamed/"+cell[j]+".png").copyTo(Gdx.files.local("docs/animated/"+cell[j]+".png"));
                            sb.append(cell[j]).append(": <img src=\"animated/").append(cell[j]).append(".png\" />\n");
                        }
                    }
                    sb.append("</p>\n");
                }
            }
        }
        sb.append("</body>\n" + "</html>\n");
        String sbs = sb.toString();
        Gdx.files.local("docs/index.html").writeString(sbs, false, "UTF8");
        Gdx.files.local("docs/indexSmall.html").writeString(sbs.replace(
                "    width: 32px;\n" +
                "    height: 32px;\n", ""), false, "UTF8");

    }

    @Override
    public void create() {
        reducer = new PaletteReducer(Coloring.DB16);
        reducer.setDitherStrength(1f);
        png8 = new PNG8();
        png8.palette = reducer;
        png8.setFlipY(false);
        gif = new AnimatedGif();
        gif.palette = reducer;
        gif.setFlipY(false);
//        split();
        animate();
        Gdx.app.exit();
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
    }
}
