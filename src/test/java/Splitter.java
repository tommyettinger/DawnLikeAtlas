import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import dawnliker.Coloring;
import dawnliker.PNG8;
import dawnliker.PaletteReducer;

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

    public static final String[] listing = {
//            "Ammo",
//            "Amulet",
//            "Aquatic0",
//            "Armor",
            "Avian0",
//            "Book",
//            "Boot",
//            "Cat0",
//            "Chest0",
//            "Decor0",
//            "Demon0",
//            "Dog0",
//            "Door0",
//            "Effect0",
//            "Elemental0",
//            "Engineer",
//            "Engineer_Clothes",
//            "Fence",
//            "Flesh",
//            "Floor",
//            "Food",
//            "GUI0",
//            "Glove",
//            "Ground0",
//            "Hat",
//            "Hill0",
//            "Humanoid0",
//            "Icons",
//            "Key",
//            "Light",
//            "Logo",
//            "LongWep",
//            "Mage",
//            "Mage_Clothes",
//            "Map0",
//            "MedWep",
//            "Misc0",
//            "Money",
//            "Music",
//            "Ore0",
//            "Paladin",
//            "Paladin_Clothes",
//            "Pest0",
//            "Pit0",
//            "Plant0",
//            "Player0",
//            "Potion",
//            "Quadruped0",
//            "Reptile0",
//            "Ring",
//            "Rock",
//            "Rodent0",
//            "Rogue",
//            "Rogue_Clothes",
//            "Scroll",
//            "Shield",
//            "ShortWep",
//            "Slime0",
//            "Template",
//            "Tile",
//            "Tool",
//            "Trap0",
//            "Tree0",
//            "Undead0",
//            "Wall",
//            "Wand",
//            "Warrior",
//            "Warrior_Clothes",
//            "Warrior_Clothes_Back"
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
//            FileHandle txt = Gdx.files.local("src/test/resources/" + name + ".txt");
//            if(!txt.exists())
//            {
//                txt.writeString("", false);
//                continue;
//            }
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

    @Override
    public void create() {
        reducer = new PaletteReducer(Coloring.DB16);
        reducer.setDitherStrength(1f);
        png8 = new PNG8();
        png8.palette = reducer;
        png8.setFlipY(false);
        split();
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
