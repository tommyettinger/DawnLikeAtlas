import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.github.tommyettinger.anim8.PNG8;
import com.github.tommyettinger.anim8.PaletteReducer;
import dawnliker.Coloring;

public class Palettizer extends ApplicationAdapter {
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
//            "Ammo.png",
//            "Amulet.png",
//            "Aquatic0.png",
//            "Aquatic1.png",
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
            "Effect0.png",
            "Effect1.png",
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
//            "Warrior_Clothes_Back.png",
    }, altListing = {
            "Dungeon.png"
    };
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Palette Reducer");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        final Palettizer app = new Palettizer();
        new Lwjgl3Application(app, config);
    }

    public void split() {
        {
            Gdx.files.local("renamed").mkdirs();
            Pixmap p = new Pixmap(Gdx.files.internal("clumped/Effect0.png"));
            png8.writePreciseSection(Gdx.files.local("renamed/pixel.png"),
                    p, Coloring.DB16, 24, 24, 1, 1);
        }

        for(String name : listing) {
            Pixmap p = new Pixmap(Gdx.files.internal("clumped/" + name));
            png8.writePrecisely(Gdx.files.local("flat/" + name), p, Coloring.DB16, true, 0);
            int frame = -1;
            String abbr;
            if(name.endsWith("0.png")){
                abbr = name.substring(0, name.length() - 5);
                frame = 0;
            }
            else if(name.endsWith("1.png")){
                abbr = name.substring(0, name.length() - 5);
                frame = 1;
            }
            else
                abbr = name.substring(0, name.length() - 4);
            int w = p.getWidth() >>> 4, h = p.getHeight() >>> 4;
            Gdx.files.local("individual").mkdirs();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if(frame >= 0)
                        png8.writePreciseSection(Gdx.files.local("individual/"+abbr+"_"+x+"x"+y+"_"+frame+".png"),
                                p, Coloring.DB16, x<<4, y<<4, 16, 16);
                    else
                        png8.writePreciseSection(Gdx.files.local("individual/"+abbr+"_"+x+"x"+y+".png"),
                                p, Coloring.DB16, x<<4, y<<4, 16, 16);
                }
            }
        }

        //unused
//            for(String name : altListing) {
//                Pixmap p = new Pixmap(Gdx.files.internal("clumped/" + name));
//                reducer.analyze(p, 0);
////                png8.writePrecisely(Gdx.files.local("flat/" + name), p, true, 0);
//                int frame = -1;
//                String abbr = name.substring(0, name.length() - 4);
//                int w = p.getWidth() >>> 4, h = p.getHeight() >>> 4;
//                Gdx.files.local("altIndividual").mkdirs();
//                for (int y = 0; y < h; y++) {
//                    for (int x = 0; x < w; x++) {
//                        png8.writePreciseSection(Gdx.files.local("altIndividual/"+abbr+"_"+x+"x"+y+".png"),
//                                p, null, x<<4, y<<4, 16, 16);
//                    }
//                }
//            }
    }

    @Override
    public void create() {
//        font = new BitmapFont();
//        batch = new SpriteBatch();
        reducer = //new PaletteReducer(Colorizer.JudgeBonusPalette);
                //Coloring.FLESURRECT_REDUCER;
                new PaletteReducer(Coloring.DB16);
                //Colorizer.RinsedColorizer.getReducer();
                // new PaletteReducer(Coloring.RINSED);
        reducer.setDitherStrength(1f);
        png8 = new PNG8();
        png8.palette = reducer;
        png8.setFlipY(false);
        split();
        Gdx.app.exit();
//        screenView = new ScreenViewport();
//        screenView.getCamera().position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 0);
//        screenView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        batch.enableBlending();
//        Gdx.input.setInputProcessor(inputProcessor());
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        batch.setProjectionMatrix(screenView.getCamera().combined);
//        batch.begin();
//        if(screenTexture != null)
//            batch.draw(screenTexture, 0, 0);
//        else {
//            font.draw(batch, "Drag and drop an image file onto this window;", 20, 150);
//            font.draw(batch, "a palette-reduced copy will be written to this folder.", 20, 120);
//        }
//        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

//    public InputProcessor inputProcessor() {
//        return new InputAdapter() {
//            @Override
//            public boolean keyDown(int keycode) {
//                switch (keycode) {
//                    case Input.Keys.Q:
//                    case Input.Keys.ESCAPE:
//                        Gdx.app.exit();
//                        break;
//                }
//                return true;
//            }
//        };
//    }
}
