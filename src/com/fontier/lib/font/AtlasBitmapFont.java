package com.fontier.lib.font;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Represents a {@link BitmapFont} but instead of having all rendered chars buffered separately 
 * they all get merged into a single image (which could be used as a texture for text rendering)
 * 
 * @author ShietStone
 */
public class AtlasBitmapFont {

    private BufferedImage bitmap;
    private int[] glyphsX;
    private int[] glyphsWidth;
    
    /**
     * Initializes this object using the given {@link BitmapFont}, which may not be null.
     * 
     * @param bitmapFont The {@link BitmapFont} to use the render data from
     */
    public AtlasBitmapFont(BitmapFont bitmapFont) {
        int bitmapWidth = 0;
        BufferedImage[] glyphs = bitmapFont.getGlyphs();
        
        for(int index = 0; index < glyphs.length; index++)
            if(glyphs[index] != null)
                bitmapWidth += glyphs[index].getWidth();
        
        bitmap = new BufferedImage(bitmapWidth, bitmapFont.getGlyphHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bitmap.createGraphics();
        glyphsX = new int[glyphs.length];
        glyphsWidth = new int[glyphs.length];
        
        int x = 0;
        for(int index = 0; index < glyphs.length; index++)  {        
            glyphsX[index] = x;
            glyphsWidth[index] = 0;
            
            BufferedImage glyph = glyphs[index];
            
            if(glyph != null) {
                graphics.drawImage(glyph, x, 0, null);
                x += glyph.getWidth();
                glyphsWidth[index] = glyph.getWidth();
            }
        }
        
        graphics.dispose();
    }
    
    /**
     * Returns the image containing the whole rendered {@link BitmapFont}.
     * 
     * @return The image containing the render data
     */
    public BufferedImage getBitmap() {
        return bitmap;
    }
    
    /**
     * Returns the array with each chars x position on the image (ordered by ASCII/Unicode value).
     * If a char could not be rendered (e.g. the new-line char) it will have the same x value as
     * the last previous char.
     * 
     * @return The x positions of all chars contained in the given {@link BitmapFont}
     */
    public int[] getGlyphsX() {
        return glyphsX;
    }
    
    /**
     * Returns the array with each chars width on the image (ordered by ASCII/Unicode value). If a 
     * char could not be rendered (e.g. the new-line char) it will have 0 as width.
     * 
     * @return The width of all chars contained in the given {@link BitmapFont}
     */
    public int[] getGlyphsWidth() {
        return glyphsWidth;
    }
}
