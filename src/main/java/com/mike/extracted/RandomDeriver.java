package com.mike.extracted;

import com.mike.recreated.Identifier;
import com.mike.recreated.BlockPos;

@SuppressWarnings("unused")
public interface RandomDeriver {
    default AbstractRandom createRandom(BlockPos pos) {
        return this.createRandom(pos.x, pos.y, pos.z);
    }

    default AbstractRandom createRandom(Identifier id) {
        return this.createRandom(id.toString());
    }

    AbstractRandom createRandom(String var1);

    AbstractRandom createRandom(int var1, int var2, int var3);
}

