/*
 * "Copyright (c) 2014   Capgemini Technology Services (hereinafter "Capgemini")
 *
 * License/Terms of Use
 * Permission is hereby granted, free of charge and for the term of intellectual
 * property rights on the Software, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to use, copy, modify and
 * propagate free of charge, anywhere in the world, all or part of the Software
 * subject to the following mandatory conditions:
 *
 * -   The above copyright notice and this permission notice shall be included in
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


package com.github.wuic.engine;

import com.github.wuic.ContextBuilder;
import com.github.wuic.ContextBuilderConfigurator;
import com.github.wuic.engine.core.*;
import com.github.wuic.exception.BuilderPropertyNotSupportedException;
import com.github.wuic.exception.UnableToInstantiateException;
import com.github.wuic.exception.wrapper.StreamException;
import com.github.wuic.util.AbstractBuilderFactory;

/**
 * <p>
 * Factory for {@link com.github.wuic.engine.EngineBuilder}.
 * </p>
 *
 * @author Guillaume DROUET
 * @version 1.2
 * @since 0.4.0
 */
public final class EngineBuilderFactory extends AbstractBuilderFactory<EngineBuilder> {

    /**
     * All class names to search in classpath.
     */
    private static final String[] LOOKUP_CLASSES = {
            EhCacheEngineBuilder.class.getName(),
            MemoryMapCacheEngineBuilder.class.getName(),
            YuiCompressorJavascriptEngineBuilder.class.getName(),
            YuiCompressorCssEngineBuilder.class.getName(),
            CssInspectorEngineBuilder.class.getName(),
            TextAggregatorEngineBuilder.class.getName(),
            ImageCompressorEngineBuilder.class.getName(),
            ImageAggregatorEngineBuilder.class.getName(),
            StaticEngineBuilder.class.getName(),
            HtmlInspectorEngineBuilder.class.getName(),
            SpriteInspectorEngineBuilder.class.getName(),
    };

    /**
     * Unique instance.
     */
    private static EngineBuilderFactory instance = null;

    /**
     * <p>
     * Gets the unique instance of this class.
     * </p>
     *
     * @return the singleton
     */
    public static EngineBuilderFactory getInstance() {
        if (instance == null) {
            instance = new EngineBuilderFactory();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    protected String[] classes() {
        return LOOKUP_CLASSES;
    }

    /**
     * <p>
     * Creates a {@link ContextBuilderConfigurator} which configures a default instance of each type of supported engine.
     * </p>
     *
     * @return the new instance
     */
    public ContextBuilderConfigurator newContextBuilderConfigurator() {
        return new DefaultEngineContextBuilderConfigurator();
    }

    /**
     * <p>
     * This class configures default engines in the {@link com.github.wuic.ContextBuilder}.
     * </p>
     *
     * @author Guillaume DROUET
     * @version 1.0
     * @since 0.4.0
     */
    private final class DefaultEngineContextBuilderConfigurator extends ContextBuilderConfigurator {

        /**
         * {@inheritDoc}
         */
        @Override
        public int internalConfigure(final ContextBuilder ctxBuilder) {
            try {
                for (final String type : EngineBuilderFactory.this.knownTypes()) {
                    ctxBuilder.contextEngineBuilder(ID_PREFIX + type, type).toContext();
                }
                // Should never occur
            } catch (BuilderPropertyNotSupportedException bpnse) {
                throw new IllegalStateException(bpnse);
            } catch (UnableToInstantiateException itie) {
                throw new IllegalStateException(itie);
            }

            // Never poll
            return -1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTag() {
            return "default.engine.core";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Long getLastUpdateTimestampFor(final String path) throws StreamException {
            // Never poll
            return 1L;
        }
    }
}
