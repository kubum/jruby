/*
 * Copyright (c) 2014, 2015 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.jruby.truffle.nodes.methods;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.source.SourceSection;
import org.jruby.truffle.nodes.RubyNode;
import org.jruby.truffle.nodes.core.ModuleNodes;
import org.jruby.truffle.runtime.RubyContext;
import org.jruby.truffle.runtime.core.RubyBasicObject;

public class UndefNode extends RubyNode {

    @Child private RubyNode module;
    final String name;

    public UndefNode(RubyContext context, SourceSection sourceSection, RubyNode module, String name) {
        super(context, sourceSection);
        this.module = module;
        this.name = name;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        CompilerDirectives.transferToInterpreter();

        final RubyBasicObject moduleObject;

        try {
            moduleObject = module.executeRubyBasicObject(frame);
        } catch (UnexpectedResultException e) {
            throw new RuntimeException(e);
        }

        ModuleNodes.getModel(moduleObject).undefMethod(this, name);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        executeVoid(frame);
        return nil();
    }

}
