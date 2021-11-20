package com.fontier.lib.font;

import java.awt.Font;
import java.awt.image.BufferedImage;

/**
 * Represents a {@link Font} object but in a finalized rendered form.
 * 
 * @author ShietStone
 */
public class BitmapFont {

    private BufferedImage[] glyphs;
    private int ascent;
    private int descent;
    private int baseLine;
    private int glyphHeight;
    
    /**
     * Initializes this {@link BitmapFont} using the given {@link Font} and the amount of chars to 
     * render. Throws an {@link IllegalArgumentException} if the font is null or the char count is
     * not greater than zero. All rendered chars are  to have the same height. 
     * 
     * @param font The {@link Font} to render the chars of
     * @param charCount The amount of chars to render
     * @param manualCharWidth If the char width will be calculated manually by the
     * 		 				  {@link BitmapRenderer}
     */
    public BitmapFont(Font font, int charCount, boolean manualCharWidth) {
        if(font == null)
            throw new IllegalArgumentException("Font is null");
        
        if(charCount <= 0)
            throw new IllegalArgumentException("Char count must be greater than zero");
        
        BitmapRenderer bitmapRenderer = new BitmapRenderer(font, manualCharWidth);
        ascent = bitmapRenderer.getAscent();
        descent = bitmapRenderer.getDescent();
        baseLine = bitmapRenderer.getBaseLine();
        glyphs = new BufferedImage[charCount];
        
        for(int index = 0; index < charCount; index++) {
            BufferedImage glyph = bitmapRenderer.renderGlyph((char) index); 
            glyphs[index] = glyph;
            
            if(glyph != null)
                glyphHeight = glyph.getHeight();
        }
        
        bitmapRenderer.dispose();
    }
    
    /**
     * Initializes this {@link BitmapFont} using the given {@link Font} and and default values for
     * all other arguments. Throws an {@link IllegalArgumentException} if the font is null. All 
     * rendered chars are  to have the same height.
     * 
     * @param font The {@link Font} to render the chars of
     */
    public BitmapFont(Font font) {
    	this(font, 256, false);
    }
    
    /**
     * Returns the base line y coordinate, meaning the distance from the top of the rendered chars
     * to where the baseline would be.
     * 
     * @return The bitmaps base line y coordinate
     */
    public int getBaseLine() {
        return baseLine;
    }
    
    /**
     * Returns the default ascent of the {@link Font} relative to the base line. Some chars exceed
     * the default ascent, thats why this ascent value is usually less than the chars bitmap height
     * relative to the baseline.
     * 
     * @return The default ascent of the {@link Font}
     */
    public int getAscent() {
        return ascent;
    }
    
    /**
     * Returns the default descent of the {@link Font} relative to the base line. Some chars exceed
     * the default descent, thats why this descent value is usually less than the chars bitmap 
     * height relative to the baseline.
     * 
     * @return The default descent of the {@link Font}
     */
    public int getDescent() {
        return descent;
    }
    
    /**
     * Returns the full height of a chars bitmap. This is the maximum ascent plus maximum descent.
     * This will usually be greater than getAscent() + getDescent() as only a few chars exceed
     * the default ascent and descent. Still, every rendered char will have the same height even
     * when not exceeding these default values.
     * 
     * @return The total height of a rendered char
     */
    public int getGlyphHeight() {
        return glyphHeight;
    }
    
    /**
     * Returns the array containing all rendered chars in ASCII/Unicode order. Some elements may be
     * null if the char can not be rendered (like the new-line char).
     * 
     * @return The array containing the rendered chars
     */
    public BufferedImage[] getGlyphs() {
        return glyphs;
    }
}
