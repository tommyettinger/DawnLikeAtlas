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
            "Aquatic0",
//            "Armor.png",
//            "Avian0.png",
//            "Avian1.png",
//            "Book.png",
//            "Boot.png",
//            "Cat0.png",
//            "Cat1.png",
//            "Chest0.png",
//            "Chest1.png",
//            "Decor0.png",
//            "Decor1.png",
//            "Demon0.png",
//            "Demon1.png",
//            "Dog0.png",
//            "Dog1.png",
//            "Door0.png",
//            "Door1.png",
//            "Effect0.png",
//            "Effect1.png",
//            "Elemental0.png",
//            "Elemental1.png",
//            "Engineer.png",
//            "Engineer_Clothes.png",
//            "Fence.png",
//            "Flesh.png",
//            "Floor.png",
//            "Food.png",
//            "GUI0.png",
//            "GUI1.png",
//            "Glove.png",
//            "Ground0.png",
//            "Ground1.png",
//            "Hat.png",
//            "Hill0.png",
//            "Hill1.png",
//            "Humanoid0.png",
//            "Humanoid1.png",
//            "Icons.png",
//            "Key.png",
//            "Light.png",
//            "Logo.png",
//            "LongWep.png",
//            "Mage.png",
//            "Mage_Clothes.png",
//            "Map0.png",
//            "Map1.png",
//            "MedWep.png",
//            "Misc0.png",
//            "Misc1.png",
//            "Money.png",
//            "Music.png",
//            "Ore0.png",
//            "Ore1.png",
//            "Paladin.png",
//            "Paladin_Clothes.png",
//            "Pest0.png",
//            "Pest1.png",
//            "Pit0.png",
//            "Pit1.png",
//            "Plant0.png",
//            "Plant1.png",
//            "Player0.png",
//            "Player1.png",
//            "Potion.png",
//            "Quadruped0.png",
//            "Quadruped1.png",
//            "Reptile0.png",
//            "Reptile1.png",
//            "Ring.png",
//            "Rock.png",
//            "Rodent0.png",
//            "Rodent1.png",
//            "Rogue.png",
//            "Rogue_Clothes.png",
//            "Scroll.png",
//            "Shield.png",
//            "ShortWep.png",
//            "Slime0.png",
//            "Slime1.png",
//            "Template.png",
//            "Tile.png",
//            "Tool.png",
//            "Trap0.png",
//            "Trap1.png",
//            "Tree0.png",
//            "Tree1.png",
//            "Undead0.png",
//            "Undead1.png",
//            "Wall.png",
//            "Wand.png",
//            "Warrior.png",
//            "Warrior_Clothes.png",
//            "Warrior_Clothes_Back.png"
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
