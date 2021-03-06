/*
 * Copyright (c) 2018, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package com.oracle.truffle.regex.tregex.nodes.dfa;

public final class TRegexDFAExecutorProperties {

    private final boolean forward;
    private final boolean searching;
    private final boolean trackCaptureGroups;
    private final boolean regressionTestMode;
    private final int numberOfCaptureGroups;
    private final int minResultLength;

    public TRegexDFAExecutorProperties(
                    boolean forward,
                    boolean searching,
                    boolean trackCaptureGroups,
                    boolean regressionTestMode,
                    int numberOfCaptureGroups,
                    int minResultLength) {
        this.forward = forward;
        this.searching = searching;
        this.trackCaptureGroups = trackCaptureGroups;
        this.regressionTestMode = regressionTestMode;
        this.numberOfCaptureGroups = numberOfCaptureGroups;
        this.minResultLength = minResultLength;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBackward() {
        return !forward;
    }

    public boolean isSearching() {
        return searching;
    }

    public boolean isTrackCaptureGroups() {
        return trackCaptureGroups;
    }

    public boolean isRegressionTestMode() {
        return regressionTestMode;
    }

    public int getNumberOfCaptureGroups() {
        return numberOfCaptureGroups;
    }

    public int getMinResultLength() {
        return minResultLength;
    }
}
