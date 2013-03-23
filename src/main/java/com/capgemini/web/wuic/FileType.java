/*
 * "Copyright (c) 2013   Capgemini Technology Services (hereinafter "Capgemini")
 *
 * License/Terms of Use
 * Permission is hereby granted, free of charge and for the term of intellectual
 * property rights on the Software, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to use, copy, modify and
 * propagate free of charge, anywhere in the world, all or part of the Software
 * subject to the following mandatory conditions:
 *
 * �   The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Any failure to comply with the above shall automatically terminate the license
 * and be construed as a breach of these Terms of Use causing significant harm to
 * Capgemini.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, PEACEFUL ENJOYMENT,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Except as contained in this notice, the name of Capgemini shall not be used in
 * advertising or otherwise to promote the use or other dealings in this Software
 * without prior written authorization from Capgemini.
 *
 * These Terms of Use are subject to French law.
 *
 * IMPORTANT NOTICE: The WUIC software implements software components governed by
 * open source software licenses (BSD and Apache) of which CAPGEMINI is not the
 * author or the editor. The rights granted on the said software components are
 * governed by the specific terms and conditions specified by Apache 2.0 and BSD
 * licenses."
 */


package com.capgemini.web.wuic;

/**
 * <p>
 * Enumeration of the possible types to be compressed and / or aggregated in WUIC.
 * </p>
 * 
 * @author Guillaume DROUET
 * @version 1.0
 * @since 0.1.0
 */
public enum FileType {

    /**
     * Sprite file support.
     */
    SPRITE(new String[] {".png", ".PNG"}, null),
    
    /**
     * PNG file support.
     */
    PNG(new String[] {".png", ".PNG"}, "image/png"),
    
    /**
     * Javascript files support.
     */
    JAVASCRIPT(new String[] { ".js" }, "text/javascript"),
    
    /**
     * CSS files support.
     */
    CSS(new String[] { ".css" }, "text/css");
    
    /**
     * Possible extensions.
     */
    private String[] extensions;
    
    /**
     * MIME type.
     */
    private String mimeType;
    
    /**
     * <p>
     * Builds a new {@link FileType} according to the given extensions and the
     * given MIME type.
     * </p>
     * 
     * @param exts the extensions
     * @param mime the MIME type
     */
    FileType(final String[] exts, final String mime) {
        extensions = exts;
        mimeType = mime;
    }
    
    /**
     * <p>
     * Returns the possible extensions.
     * </p>
     * 
     * @return the possible extensions
     */
    public String[] getExtensions() {
        return extensions;
    }
    
    /**
     * <p>
     * Returns the MIME type.
     * </p>
     * 
     * @return the MIME type
     */
    public String getMimeType() {
        return mimeType;
    }
    
    /**
     * <p>
     * Parse the given {@code String} object into a {@link FileType}. If the
     * value does not equal to any exiting {@link FileType#name()}, then an
     * {@link IllegalArgumentException} will be thrown.
     * </p>
     * 
     * @param strValue the value to parse
     * @return the parsed {@link FileType}
     */
    public static FileType parseFileType(final String strValue) {
        for (FileType fileType : FileType.values()) {
            if (fileType.name().equals(strValue)) {
                return fileType;
            }
        }
        
        throw new IllegalArgumentException(strValue + " has not been recognized as a FileType");
    }
}
