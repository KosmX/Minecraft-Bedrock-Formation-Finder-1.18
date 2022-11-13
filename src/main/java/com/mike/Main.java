package com.mike;

import dev.kosmx.bedrockfinder.UtilKt;
import dev.kosmx.bedrockfinder.mc.BedrockBlock;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public class Main {
    static ArrayList<BedrockBlock> blocks = new ArrayList<>();
    static BedrockReader bedrockReader;

    public static void main(String[] args) {
        long seed = Long.parseLong(args[0]);
        final boolean aligned = args[3].equals("--aligned") || args[3].equals("-a");

        String[] coordinateString = args[1].split(":");
        int xDef = Integer.parseInt(coordinateString[0]);
        int zDef = Integer.parseInt(coordinateString[1]);

        BedrockReader.BedrockType bedrockType = switch (args[2]) {
            case "floor" -> BedrockReader.BedrockType.BEDROCK_FLOOR;
            case "nether_floor" -> BedrockReader.BedrockType.NETHER_FLOOR;
            case "roof", "nether_roof" -> BedrockReader.BedrockType.NETHER_ROOF;
            default -> throw new IllegalArgumentException("unknown type: " + args[2]);
        };

        final boolean checkInvert = true;

        Arrays.stream(args).skip(aligned ? 4 : 3).forEach((arg) -> blocks.add(BedrockBlock.Companion.invoke(arg)));
        blocks.sort((b1, b2) -> {
            switch (bedrockType) {
                case BEDROCK_FLOOR -> {
                    return b1.getY() < b2.getY() ? 1 : -1;
                }
                case NETHER_ROOF -> {
                    return b1.getY() < b2.getY() ? -1 : 1;
                }
            }
            return 0;
        });
        if (blocks.size() == 0) return;
        blocks.forEach(System.out::println);

        bedrockReader = new BedrockReader(seed, bedrockType);

        //ThreadedKt.run(UtilKt.snail(aligned, xDef, zDef), bedrockReader);


        var pattern = UtilKt.snail(aligned, xDef, zDef).iterator();
        while (pattern.hasNext()) {
            var xz = pattern.next();
            var x = xz.getFirst();
            var z = xz.getSecond();
            if (checkFormation(x, z) || (checkInvert && checkFormationInvert(x, z))) {
                System.out.println("Found Bedrock Formation at X:" + x + " Z:" + z);
                System.out.println("/tp " + x + " ~ " + z);
                break;
            }
        }
    }

    static boolean checkFormation(int x, int z) {
        for (BedrockBlock block : blocks) {
            if (bedrockReader.isBedrock(x + block.getX(), block.getY(), z + block.getZ()) != block.getShouldBeBedrock()) return false;
        }
        return true;
    }
    static boolean checkFormationInvert(int x, int z) {
        for (BedrockBlock block : blocks) {
            if (bedrockReader.isBedrock(x - 1 - block.getX(), block.getY(), z - 1 - block.getZ()) != block.getShouldBeBedrock()) return false;
        }
        return true;
    }
}
