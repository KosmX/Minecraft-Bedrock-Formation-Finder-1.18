package com.mike;

public class Test {
    public static void main(String[] args) {
        var bedrockReader = new BedrockReader(6877471587650894572L, BedrockReader.BedrockType.NETHER_FLOOR);
        var b = bedrockReader.isBedrock(-80, 4, 112);
        System.out.println(b);
    }
}
