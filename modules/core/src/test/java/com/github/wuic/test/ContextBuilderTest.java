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


package com.github.wuic.test;

import com.github.wuic.Context;
import com.github.wuic.ContextBuilder;
import com.github.wuic.NutType;
import com.github.wuic.engine.Engine;
import com.github.wuic.engine.EngineRequest;
import com.github.wuic.engine.EngineType;
import com.github.wuic.exception.WorkflowNotFoundException;
import com.github.wuic.engine.EngineBuilder;
import com.github.wuic.nut.Nut;
import com.github.wuic.nut.NutDao;
import com.github.wuic.nut.NutDaoBuilder;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>
 * {@link com.github.wuic.ContextBuilder} and {@link Context} tests.
 * </p>
 *
 * @author Guillaume DROUET
 * @version 1.0
 * @since 0.4.0
 */
@RunWith(JUnit4.class)
public class ContextBuilderTest {

    /**
     * A nut.
     */
    private Nut mockNutOne;

    /**
     * Another nut.
     */
    private Nut mockNutTwo;

    /**
     * A dao.
     */
    private NutDao mockDao;

    /**
     * A store.
     */
    private NutDao mockStore;

    /**
     * A DAO builder.
     */
    private NutDaoBuilder mockDaoBuilder;

    /**
     * A DAO builder that could be a store.
     */
    private NutDaoBuilder mockStoreBuilder;

    /**
     * An engine.
     */
    private Engine mockEngine;

    /**
     * An engine builder.
     */
    private EngineBuilder mockEngineBuilder;

    /**
     * Create some mocked object.
     */
    @Before
    public void prepareMocks() throws Exception {
        // Prepare Nut mock
        mockNutOne = mock(Nut.class);
        when(mockNutOne.getName()).thenReturn("foo.js");
        when(mockNutOne.getNutType()).thenReturn(NutType.JAVASCRIPT);
        when(mockNutOne.isAggregatable()).thenReturn(true);
        when(mockNutOne.isTextCompressible()).thenReturn(true);
        when(mockNutOne.isCacheable()).thenReturn(true);
        when(mockNutOne.isBinaryCompressible()).thenReturn(true);
        when(mockNutOne.openStream()).thenReturn(new ByteArrayInputStream("var foo;".getBytes()));

        mockNutTwo = mock(Nut.class);
        when(mockNutTwo.getName()).thenReturn("test.js");
        when(mockNutTwo.getNutType()).thenReturn(NutType.JAVASCRIPT);
        when(mockNutTwo.isAggregatable()).thenReturn(true);
        when(mockNutTwo.isTextCompressible()).thenReturn(true);
        when(mockNutTwo.isCacheable()).thenReturn(true);
        when(mockNutTwo.isBinaryCompressible()).thenReturn(true);
        when(mockNutTwo.openStream()).thenReturn(new ByteArrayInputStream("var test;".getBytes()));

        // Prepare DAO mock
        mockDao = mock(NutDao.class);
        when(mockDao.create(mockNutOne.getName())).thenReturn(Arrays.asList(mockNutOne));
        when(mockDao.create(mockNutTwo.getName())).thenReturn(Arrays.asList(mockNutTwo));
        when(mockDao.saveSupported()).thenReturn(false);
        mockStore = mock(NutDao.class);
        when(mockStore.saveSupported()).thenReturn(true);

        // Prepare DAO builder mock
        mockDaoBuilder = mock(NutDaoBuilder.class);
        when(mockDaoBuilder.build()).thenReturn(mockDao);
        mockStoreBuilder = mock(NutDaoBuilder.class);
        when(mockStoreBuilder.build()).thenReturn(mockStore);

        // Prepare Engine mock
        mockEngine = mock(Engine.class);
        when(mockEngine.getNutTypes()).thenReturn(Arrays.asList(NutType.JAVASCRIPT));
        when(mockEngine.getEngineType()).thenReturn(EngineType.AGGREGATOR);
        when(mockEngine.parse(any(EngineRequest.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocationOnMock) throws Throwable {
                return ((EngineRequest) invocationOnMock.getArguments()[0]).getResources();
            }
        });

        // Prepare Engine builder mock
        mockEngineBuilder = mock(EngineBuilder.class);
        when(mockEngineBuilder.build()).thenReturn(mockEngine);
    }

    /**
     * Nominal test with classic use.
     *
     * @throws Exception if test fails
     */
    @Test
    public void nominalTest() throws Exception {
        // Typical use : no exception should be thrown
        final Context context = new ContextBuilder()
                .tag("test")
                .nutDaoBuilder("dao", mockDaoBuilder, new HashMap<String, Object>())
                .heap("heap", "dao", mockNutOne.getName(), mockNutTwo.getName())
                .engineBuilder("engine", mockEngineBuilder, new HashMap<String, Object>())
                .workflow("workflow", "heap", new String[]{"engine"})
                .releaseTag()
                .build();

        // Should be aggregated now
        Assert.assertTrue(context.process("workflow", "").size() == 1);
    }

    /**
     * Nominal test with classic use but without default engine inclusion.
     *
     * @throws Exception if test fails
     */
    @Test
    public void withoutDefaultEnginesTest() throws Exception {
        // Typical use : no exception should be thrown
        final Context context = new ContextBuilder()
                .tag("test")
                .nutDaoBuilder("dao", mockDaoBuilder, new HashMap<String, Object>())
                .heap("heap", "dao", mockNutOne.getName(), mockNutTwo.getName())
                .engineBuilder("engine", mockEngineBuilder, new HashMap<String, Object>())
                .workflow("workflow", "heap", new String[]{"engine"}, false)
                .releaseTag()
                .build();

        Assert.assertTrue(context.process("workflow", "").size() > 1);
    }

    /**
     * Checks when the context is up to date or not.
     */
    @Test
    public void upToDateTest() {
        final ContextBuilder builder = new ContextBuilder();
        final Context context = builder.build();
        Assert.assertTrue(context.isUpToDate());

        Assert.assertTrue(builder.tag("test").releaseTag().build().isUpToDate());
        Assert.assertFalse(context.isUpToDate());
    }


    /**
     * Checks when the context is configured with DAO stores.
     *
     * @throws Exception if test fails
     */
    @Test
    public void withStoreTest() throws Exception {
        try {
            new ContextBuilder()
                    .tag("test")
                    .nutDaoBuilder("dao", mockDaoBuilder, new HashMap<String, Object>())
                    .heap("heap", "dao", mockNutOne.getName(), mockNutTwo.getName())
                    .engineBuilder("engine", mockEngineBuilder, new HashMap<String, Object>())
                    .workflow("workflow", "heap", new String[]{"engine"}, "dao")
                    .releaseTag()
                    .build();
        } catch (Exception e) {
            // Normal behavior : mockDaoBuilder does not supports save()
        }

        new ContextBuilder()
                .tag("test")
                .nutDaoBuilder("dao", mockDaoBuilder, new HashMap<String, Object>())
                .nutDaoBuilder("store", mockStoreBuilder, new HashMap<String, Object>())
                .heap("heap", "dao", mockNutOne.getName(), mockNutTwo.getName())
                .engineBuilder("engine", mockEngineBuilder, new HashMap<String, Object>())
                .workflow("workflow", "heap", new String[]{"engine"}, "store")
                .releaseTag()
                .build();
    }

    /**
     * Test settings erasure.
     * @throws Exception if test fails
     */
    @Test
    public void testClearTag() throws Exception {
        final ContextBuilder builder = new ContextBuilder()
                .tag("test")
                .nutDaoBuilder("dao", mockDaoBuilder, new HashMap<String, Object>())
                .heap("heap", "dao", mockNutOne.getName(), mockNutTwo.getName())
                .engineBuilder("engine", mockEngineBuilder, new HashMap<String, Object>())
                .releaseTag()
                .tag("tag")
                .workflow("workflow", "heap", new String[]{"engine"})
                .releaseTag();
        builder.build().process("workflow", "");

        try {
            builder.clearTag("tag").build().process("workflow", "");
            Assert.fail();
        } catch (WorkflowNotFoundException wnfe) {
            // Exception normally raised
        }

        builder.tag("test")
                .workflow("workflow", "heap", new String[]{"engine"})
                .releaseTag()
                .build()
                .process("workflow", "");

        try {
            builder.clearTag("test").build().process("workflow", "");
            Assert.fail();
        } catch (WorkflowNotFoundException wnfe) {
            // Exception normally raised
        }
    }

    /**
     * Test when a builder is used without any tag.
     */
    @Test
    public void unTaggedUsageTest() {
        try {
            new ContextBuilder().nutDaoBuilder("dao", mock(NutDaoBuilder.class), new HashMap<String, Object>());
        } catch (Exception e) {
            // Normal behavior
        }
    }

    /**
     * Test some concurrent accesses.
     *
     * @throws InterruptedException if test fails
     */
    @Test
    public void concurrentTest() throws InterruptedException {
        final ContextBuilder builder = new ContextBuilder();
        final int threadNumber = 2000;
        final Thread[] threads = new Thread[threadNumber];

        for (int i = 0; i < threadNumber; i = i + 4) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        builder.tag("test")
                            .nutDaoBuilder("dao", mockDaoBuilder, new HashMap<String, Object>())
                            .heap("heap", "dao", mockNutOne.getName(), mockNutTwo.getName())
                            .engineBuilder("engine", mockEngineBuilder, new HashMap<String, Object>())
                            .workflow("workflow", "heap", new String[]{"engine"})
                            .releaseTag()
                            .build();
                    } catch (Exception e) {
                        System.out.println(Thread.currentThread().toString());
                        e.printStackTrace(System.out);
                        Assert.fail();
                    }
                }
            });

            threads[i + 1] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        builder.tag("foo")
                                .nutDaoBuilder("dao", mockDaoBuilder, new HashMap<String, Object>())
                                .heap("heap", "dao", mockNutOne.getName(), mockNutTwo.getName())
                                .engineBuilder("engine", mockEngineBuilder, new HashMap<String, Object>())
                                .workflow("workflow", "heap", new String[]{"engine"})
                                .releaseTag()
                                .build();
                    } catch (Exception e) {
                        Assert.fail();
                    }
                }
            });

            threads[i + 2] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        builder.clearTag("test");
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        Assert.fail();
                    }
                }
            });

            threads[i + 3] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        builder.clearTag("foo");
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        Assert.fail();
                    }
                }
            });
        }

        // Start all threads
        for (Thread t : threads) {
            t.start();
        }

        // Wait all threads
        for (Thread t : threads) {
            t.join();
        }
    }
}
