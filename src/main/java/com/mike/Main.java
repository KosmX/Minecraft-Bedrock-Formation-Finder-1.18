package com.mike;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static ArrayList<BedrockBlock> blocks = new ArrayList<>();
    static BedrockReader bedrockReader;

    public static void main(String[] args) {
        long seed = Long.parseLong(args[0]);
        final boolean aligned = args[3].equals("--aligned") || args[3].equals("-a");

        String[] coordinateString = args[1].split(":");
        int x = Integer.parseInt(coordinateString[0]);
        int z = Integer.parseInt(coordinateString[1]);

        BedrockReader.BedrockType bedrockType = switch (args[2]) {
            case "floor" -> BedrockReader.BedrockType.BEDROCK_FLOOR;
            case "nether_floor" -> BedrockReader.BedrockType.NETHER_FLOOR;
            case "roof", "nether_roof" -> BedrockReader.BedrockType.BEDROCK_ROOF;
            default -> throw new IllegalArgumentException("unknown type: " + args[2]);
        };

        final boolean checkInvert = true;

        Arrays.stream(args).skip(aligned ? 4 : 3).forEach((arg) -> blocks.add(new BedrockBlock(arg)));
        blocks.sort((b1, b2) -> {
            switch (bedrockType) {
                case BEDROCK_FLOOR -> {
                    return b1.y < b2.y ? 1 : -1;
                }
                case BEDROCK_ROOF -> {
                    return b1.y < b2.y ? -1 : 1;
                }
            }
            return 0;
        });
        if (blocks.size() == 0) return;
        blocks.forEach(System.out::println);

        bedrockReader = new BedrockReader(seed, bedrockType);

        Direction direction = Direction.RIGHT;
        int stepsToTake = 1;
        int stepsTaken = 0;
        int sidesUntilIncremental = 0;
        int extraDivision = 0;

        while (true) {
            if (checkFormation(x, z) || (checkInvert && checkFormationInvert(x, z))) {
                System.out.println("Found Bedrock Formation at X:" + x + " Z:" + z);
                System.out.println("/tp " + x + " ~ " + z);
                //break;
            }

            // Check for direction change
            if (stepsTaken >= stepsToTake) {
                stepsTaken = 0;
                sidesUntilIncremental++;
                switch (direction) {
                    case LEFT -> direction = Direction.DOWN;
                    case RIGHT -> direction = Direction.UP;
                    case UP -> direction = Direction.LEFT;
                    case DOWN -> direction = Direction.RIGHT;
                }
            }

            // Increase steps to take
            if (sidesUntilIncremental > 2) {
                sidesUntilIncremental = 0;
                stepsToTake++;
                if (extraDivision++ > 8192) {
                    extraDivision = 0;
                    System.out.println(x + ";" + z);
                }
            }

            // Make Step
            if (aligned) {
                switch (direction) {
                    case LEFT -> x -= 16;
                    case RIGHT -> x += 16;
                    case UP -> z += 16;
                    case DOWN -> z -= 16;
                }
            } else {
                switch (direction) {
                    case LEFT -> x--;
                    case RIGHT -> x++;
                    case UP -> z++;
                    case DOWN -> z--;
                }
            }
            stepsTaken++;
        }
    }

    static boolean checkFormation(int x, int z) {
        for (BedrockBlock block : blocks) {
            if (bedrockReader.isBedrock(x + block.x, block.y, z + block.z) != block.shouldBeBedrock) return false;
        }
        return true;
    }
    static boolean checkFormationInvert(int x, int z) {
        for (BedrockBlock block : blocks) {
            if (bedrockReader.isBedrock(x - 1 - block.x, block.y, z - 1 - block.z) != block.shouldBeBedrock) return false;
        }
        return true;
    }

    enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
