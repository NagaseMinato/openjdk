/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */


/*
  @test
  @bug 8043610
  @summary Tests that JComponent invalidate, revalidate and repaint methods could
           be called from any thread
  @author Petr Pchelko
*/

import sun.awt.SunToolkit;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class bug8043610 {
    private static volatile JFrame frame;
    private static volatile JComponent component;

    public static void main(String[] args) throws Exception {
        ThreadGroup stubTG = new ThreadGroup(getRootThreadGroup(), "Stub Thread Group");
        ThreadGroup swingTG = new ThreadGroup(getRootThreadGroup(), "SwingTG");
        try {
            Thread stubThread = new Thread(stubTG, new Runnable() {
                @Override
                public void run() {
                    SunToolkit.createNewAppContext();
                }
            });
            stubThread.start();
            stubThread.join();

            final CountDownLatch startSwingLatch = new CountDownLatch(1);
            new Thread(swingTG, new Runnable() {
                @Override
                public void run() {
                    SunToolkit.createNewAppContext();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            frame = new JFrame();
                            component = new JLabel("Test Text");
                            frame.add(component);
                            frame.setBounds(100, 100, 100, 100);
                            frame.setVisible(true);
                            startSwingLatch.countDown();
                        }
                    });
                }
            }).start();
            startSwingLatch.await();

            final AtomicReference<Exception> caughtException = new AtomicReference<>();
            Thread checkThread = new Thread(getRootThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    try {
                        component.invalidate();
                        component.revalidate();
                        component.repaint(new Rectangle(0, 0, 0, 0));
                    } catch (Exception e) {
                        caughtException.set(e);
                    }
                }
            });
            checkThread.start();
            checkThread.join();

            if (caughtException.get() != null) {
                throw new RuntimeException("Failed. Caught exception!", caughtException.get());
            }
        } finally {
            new Thread(swingTG, new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (frame != null) {
                                frame.dispose();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    private static ThreadGroup getRootThreadGroup() {
        ThreadGroup currentTG = Thread.currentThread().getThreadGroup();
        ThreadGroup parentTG = currentTG.getParent();
        while (parentTG != null) {
            currentTG = parentTG;
            parentTG = currentTG.getParent();
        }
        return currentTG;
    }
}
