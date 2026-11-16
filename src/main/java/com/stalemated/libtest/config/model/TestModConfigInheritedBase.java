package com.stalemated.libtest.config.model;

import com.stalemated.lib.config.annotation.Comment;

public abstract class TestModConfigInheritedBase {

    @Comment("Declared in the parent class, so it must come first in the file and on the screen.")
    public int upperLevel = 3;

    @Comment("Declared in the parent class after upperLevel.")
    public boolean upperFlag = true;
}
