/*
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
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

import java.util.Arrays;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public final class TraceFinderResult extends LazyResult {

    private final int[] indices;
    private final CallTarget traceFinderCallTarget;
    @CompilationFinal(dimensions = 1) private final PreCalculatedResultFactory[] preCalculatedResults;
    private boolean resultCalculated = false;

    public TraceFinderResult(Object input, int fromIndex, int end, CallTarget traceFinderCallTarget, PreCalculatedResultFactory[] preCalculatedResults) {
        super(input, fromIndex, end);
        this.indices = new int[preCalculatedResults[0].getNumberOfGroups() * 2];
        this.traceFinderCallTarget = traceFinderCallTarget;
        this.preCalculatedResults = preCalculatedResults;
    }

    @Override
    public int getStart(int groupNumber) {
        return indices[groupNumber * 2];
    }

    @Override
    public int getEnd(int groupNumber) {
        return indices[groupNumber * 2 + 1];
    }

    public int[] getIndices() {
        return indices;
    }

    public CallTarget getTraceFinderCallTarget() {
        return traceFinderCallTarget;
    }

    public PreCalculatedResultFactory[] getPreCalculatedResults() {
        return preCalculatedResults;
    }

    public boolean isResultCalculated() {
        return resultCalculated;
    }

    public Object[] createArgsTraceFinder() {
        return new Object[]{getInput(), getEnd() - 1, getFromIndex()};
    }

    public void applyTraceFinderResult(int preCalcIndex) {
        preCalculatedResults[preCalcIndex].applyRelativeToEnd(indices, getEnd());
        resultCalculated = true;
    }

    /**
     * Forces evaluation of this lazy regex result. Do not use this method on any fast paths, use
     * {@link com.oracle.truffle.regex.runtime.nodes.TraceFinderGetResultNode} instead!
     */
    @TruffleBoundary
    @Override
    public void debugForceEvaluation() {
        if (!isResultCalculated()) {
            applyTraceFinderResult((int) traceFinderCallTarget.call(createArgsTraceFinder()));
        }
    }

    @TruffleBoundary
    @Override
    public String toString() {
        if (!isResultCalculated()) {
            debugForceEvaluation();
        }
        return Arrays.toString(indices);
    }
}
