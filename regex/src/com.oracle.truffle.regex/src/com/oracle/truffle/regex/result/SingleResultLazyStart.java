/*
 * Copyright (c) 2017, 2019, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.regex.result;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public final class SingleResultLazyStart extends LazyResult {

    private int start = -1;
    private final CallTarget findStartCallTarget;

    public SingleResultLazyStart(Object input, int fromIndex, int end, CallTarget findStartCallTarget) {
        super(input, fromIndex, end);
        this.findStartCallTarget = findStartCallTarget;
    }

    @Override
    public int getStart(int groupNumber) {
        return groupNumber == 0 ? start : -1;
    }

    @Override
    public int getEnd(int groupNumber) {
        return groupNumber == 0 ? getEnd() : -1;
    }

    public boolean isStartCalculated() {
        return start != -1;
    }

    public int getStart() {
        return start;
    }

    public CallTarget getFindStartCallTarget() {
        return findStartCallTarget;
    }

    public Object[] createArgsFindStart() {
        return new Object[]{getInput(), getEnd() - 1, getFromIndex()};
    }

    public void applyFindStartResult(int findStartResult) {
        this.start = findStartResult + 1;
    }

    /**
     * Forces evaluation of this lazy regex result. Do not use this method on any fast paths, use
     * {@link com.oracle.truffle.regex.result.RegexResultGetStartNode} instead!
     */
    @TruffleBoundary
    @Override
    public void debugForceEvaluation() {
        if (!isStartCalculated()) {
            applyFindStartResult((int) findStartCallTarget.call(createArgsFindStart()));
        }
    }

    @TruffleBoundary
    @Override
    public String toString() {
        if (!isStartCalculated()) {
            debugForceEvaluation();
        }
        return "[" + start + ", " + getEnd() + "]";
    }
}
