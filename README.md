# DawnLikeAtlas
DragonDePlatino and DawnBringer's DawnLike assets, split up and named for use in texture atlases

## What?
DawnLike is great! It is a huge set of 16x16 tiles/sprites, many with two-frame animations, meant
for roguelike and similar games. [It was originally posted on OpenGameArt](https://opengameart.org/comment/60159).
**It's CC-BY 4.0 licensed, requiring attribution to DawnBringer** (he made the 16-color palette
DawnLike uses, and DragonDePlatino specifically requires DawnBringer to receive credit). It's
probably best to credit DragonDePlatino too.

## But...
DawnLike was published as many small spritesheets, with no name given for most sprites. This makes
using it in a texture atlas a challenge. Game frameworks and engines often prefer sprites to be
supplied as one large texture, with many smaller images on it; a texture atlas provides a way to
look up a smaller image from the large texture, and may also provide grouping of related images
(like frames of the same animation). It certainly helps to have names for sprites if you're looking
them up, but it's also possible to do the lookups only by number or position.

## So!
I spent about a month, off and on, splitting up the over five thousand images in DawnLike into
separate, accurately-enough named sprites, and then I was able to pack them up into a convenient
[libGDX](https://github.com/libgdx/libgdx) atlas format using
[this GDX Texture Packer GUI](https://github.com/crashinvaders/gdx-texture-packer-gui). The atlas
includes an outlined pixel-art-style font, PlainAndSimplePlus, which I made but don't require
credit for using. It should be treated as sharing the license of this repo, CC-BY 4.0. I also
later upscaled that atlas to 2x, 3x, and 4x using a simple image scaling algorithm related to
[scale2x](https://www.scale2x.it/), available at my
[Scallop repo](https://github.com/tommyettinger/scallop). All of these original atlases are
available in the `atlases` directory. There are also newer, smaller atlases that use the libGDX
1.9.13 atlas format in the `thirteen` directory. If you want to pack an atlas yourself, **such as**
**for a different framework or engine**, then you can get the many individual images from the
`renamed` directory, and pack them however you need.

## How?
The sprites can all be previewed here on GitHub. These previews have the name before each image,
which is handy for knowing what to call a particular sprite. The different groups of sprite are
kept from the original DawnLike distribution, such as `Ammo` holding most ranged weapons and
`Aquatic` holding most water-dwelling creatures. Sprites with animation frames are shown as
animated GIFs.

 - [All sprites, with names, original size](https://tommyettinger.github.io/DawnLikeAtlas/indexSmall.html)
 - [All sprites, with names, double size](https://tommyettinger.github.io/DawnLikeAtlas/index.html)
 
## Also,
There's a simple demo that uses this, so you can see it in action in your browser,
[here on GitHub Pages](https://yellowstonegames.github.io/SquidLib-Demos/dawnlike/).
The demo scales the sprites up using Hyllian's xBR-lv3 Shader, which makes them easier to see
on high-DPI screens but loses the pixel-art details to some extent. A different demo avoids
using any scaling shader, but also happens to be used to test a color-adjustment library, so
if you try [this unscaled demo](https://yellowstonegames.github.io/SquidLib-Demos/colorful/),
you can just ignore the rapidly-sped-up day-night cycle.
