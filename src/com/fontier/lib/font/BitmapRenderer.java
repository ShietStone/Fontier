package com.fontier.lib.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * This class provides the ability to render individual chars from a given {@link Font}.
 * 
 * @author ShietStone
 */
public class BitmapRenderer {
    
    private BufferedImage image;
    private Graphics2D graphics;
    private FontMetrics fontMetrics;
    private int baseLine;
    private int subImageHeight;
    private boolean manualCharWidth;
    private boolean disposed;
    
    /**
     * Initializes this object with the given {@link Font}, which may not be null. The flag 
     * manualCharWidth determines if the width of each rendered char should be calculated manually
     * or use the value given by the {@link Font}. Sometimes the {@link Font} gives false values.
     * 
     * @param font The {@link Font} to use in the rendering process
     * @param manuaCharWidth If the char width will be calculated manually
     */
    public BitmapRenderer(Font font, boolean manualCharWidth) {
        if(font == null)
            throw new IllegalArgumentException("The font is null");
        
        image = new BufferedImage(font.getSize() * 2, font.getSize() * 2, BufferedImage.TYPE_INT_RGB);
        graphics = image.createGraphics();
        fontMetrics = graphics.getFontMetrics(font);
        baseLine = fontMetrics.getMaxAscent();
        subImageHeight = fontMetrics.getMaxAscent() + fontMetrics.getDescent();
        this.manualCharWidth = manualCharWidth;
        disposed = false;
        
        graphics.setFont(font);
    }
    
    /**
     * Initializes this object with the given {@link Font}, which may not be null. The char width
     * will not be calculated manually.
     * 
     * @param font The {@link Font} to use in the rendering process
     */
    public BitmapRenderer(Font font) {
        this(font, false);
    }
    
    /**
     * Renders and returns the given char using the {@link Font} passed in the constructor. Will
     * return null if the char can not be rendered (for example the new-line char). Will throw an
     * {@link IllegalStateException} if the renderer was already disposed.
     * 
     * @param c The char to render
     * @return A {@link BufferedImage} of the rendered char
     */
    public BufferedImage renderGlyph(char c) {
        if(disposed)
            throw new IllegalStateException("The renderer was disposed");
        
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setColor(Color.WHITE);
        graphics.drawString(c + "", 0, baseLine);

        int metricsCharWidth = fontMetrics.charWidth(c);
        int calculatedCharWidth = Math.min(findWidth(image) + findPadding(image), image.getWidth());
        int charWidth = manualCharWidth ? calculatedCharWidth : metricsCharWidth;
        
        if(charWidth == 0)
            return null;
        
        return copyImage(image.getSubimage(0, 0, charWidth, subImageHeight));
    }
    
    /**
     * Returns the base line y coordinate, meaning the distance from the top of the rendered chars
     * to where the baseline would be. Will return 0 if the renderer was already disposed.
     * 
     * @return The bitmaps base line y coordinate
     */
    public int getBaseLine() {
        if(disposed)
            return 0;
        
        return baseLine;
    }
    
    /**
     * Returns the default ascent of the {@link Font} relative to the base line. Some chars exceed
     * the default ascent, thats why this ascent value is usually less than the chars bitmap height
     * relative to the baseline. Will return 0 if the renderer was already disposed.
     * 
     * @return The default ascent of the {@link Font}
     */
    public int getAscent() {
        if(disposed)
            return 0;
        
        return fontMetrics.getAscent();
    }
    
    /**
     * Returns the default descent of the {@link Font} relative to the base line. Some chars exceed
     * the default descent, thats why this descent value is usually less than the chars bitmap 
     * height relative to the baseline. Will return 0 if the renderer was already disposed.
     * 
     * @return The default descent of the {@link Font}
     */
    public int getDescent() {
        if(disposed)
            return 0;
        
        return fontMetrics.getDescent();
    }
    
    /**
     * Disposes this object, after which it may not be used anymore. Will throw an 
     * {@link IllegalStateException} if the renderer was already disposed.
     */
    public void dispose() {
        if(disposed)
            throw new IllegalStateException("The renderer was already disposed");
        
        graphics.dispose();
    }
    
    private int findWidth(BufferedImage image) {
        for(int x = image.getWidth() - 1; x >= 0; x--)
            for(int y = 0; y < image.getHeight(); y++)
                if(new Color(image.getRGB(x, y)).getRed() > 0)
                    return x + 1;
        
        return 0;
    }
    
    private int findPadding(BufferedImage image) {
        for(int x = 0; x < image.getHeight(); x++)
            for(int y = 0; y < image.getHeight(); y++)
                if(new Color(image.getRGB(x, y)).getRed() > 0)
                    return x + 1;
        
        return 0;	
    }
    
    private BufferedImage copyImage(BufferedImage image) {
        BufferedImage nImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        for(int y = 0; y < image.getHeight(); y++)
            for(int x = 0; x < image.getWidth(); x++)
                nImage.setRGB(x, y, image.getRGB(x, y));
        
        return nImage;
    }
}
