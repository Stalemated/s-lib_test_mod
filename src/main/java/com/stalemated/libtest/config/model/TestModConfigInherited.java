package com.stalemated.libtest.config.model;

import com.stalemated.lib.config.annotation.Comment;

public class TestModConfigInherited extends TestModConfigInheritedBase {

    @Comment("Declared in the child class. It sorts first by name but must come after the parent's options.")
    public String childValue = "child";
}
