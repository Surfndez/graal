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
package com.oracle.truffle.regex.tregex.parser.ast;

import com.oracle.truffle.regex.tregex.util.json.JsonValue;

import static com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

/**
 * An assertion that succeeds depending on whether another regular expression can be matched at the
 * current position.
 * <p>
 * Corresponds to the <strong>( ? =</strong> <em>Disjunction</em> <strong>)</strong> and <strong>( ?
 * !</strong> <em>Disjunction</em> <strong>)</strong> right-hand sides of the <em>Assertion</em>
 * goal symbol in the ECMAScript RegExp syntax.
 */
public class LookAheadAssertion extends LookAroundAssertion {

    /**
     * Creates a new lookahead assertion AST node.
     *
     * Note that for this node to be complete, {@link RegexASTSubtreeRootNode#setGroup(Group)} has
     * to be called with the {@link Group} that represents the contents of this lookahead assertion.
     *
     * @param negated whether this lookahead assertion is negative or not
     */
    LookAheadAssertion(boolean negated) {
        super(negated);
    }

    private LookAheadAssertion(LookAheadAssertion copy, RegexAST ast, boolean recursive) {
        super(copy, ast, recursive);
    }

    @Override
    public LookAheadAssertion copy(RegexAST ast, boolean recursive) {
        return ast.register(new LookAheadAssertion(this, ast, recursive));
    }

    @Override
    public String getPrefix() {
        return isNegated() ? "?!" : "?=";
    }

    @Override
    public boolean equalsSemantic(RegexASTNode obj, boolean ignoreQuantifier) {
        assert !hasQuantifier();
        return this == obj || (obj instanceof LookAheadAssertion && groupEqualsSemantic((LookAheadAssertion) obj));
    }

    @TruffleBoundary
    @Override
    public JsonValue toJson() {
        return toJson(isNegated() ? "NegativeLookAheadAssertion" : "LookAheadAssertion");
    }
}
