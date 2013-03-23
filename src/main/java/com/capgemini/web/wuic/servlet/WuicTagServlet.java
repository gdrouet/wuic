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


package com.capgemini.web.wuic.servlet;

import com.capgemini.web.wuic.WuicFacade;
import com.capgemini.web.wuic.WuicResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * <p>
 * This servlet serves script that has be treated by engines.
 * The servlet has to be used when script has been generated
 * using the {@link com.capgemini.web.wuic.tag.WuicTag}. Its
 * allows to reuse files generated by the tag and evict
 * unnecessary files regeneration.
 * </p>
 *
 * @author Guillaume DROUET
 * @version 1.2
 * @since 0.1.0
 */
public class WuicTagServlet extends WuicServlet {
    
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -7678202861072625737L;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final int lastOffset = request.getRequestURI().lastIndexOf("/");
        final String urlWithoutLast = request.getRequestURI().substring(0, lastOffset);
        final String pageName = urlWithoutLast.substring(urlWithoutLast.lastIndexOf("/") + 1);
        
        if (pageName != null) {
            getPage(pageName, request, response);
        }
    }

    /**
     * <p>
     * Gets a page.
     * </p>
     * 
     * @param pageName the page name
     * @param request the request
     * @param response the response
     * @throws IOException if an I/O error occurs
     */
    private void getPage(final String pageName, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final String fileName = request.getParameter("file");
     
        // Get the files group
        final List<WuicResource> files = WuicFacade.getInstance().getGroup(pageName);
        InputStream is = null;
            
        for (WuicResource resource : files) {
            response.setContentType(resource.getFileType().getMimeType());

            if (fileName == null || (fileName != null && resource.getName().equals(fileName))) {
                try {
                    is = resource.openStream();
                    IOUtils.copy(is, response.getOutputStream());
                    is = null;
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
        }
    }    
}
