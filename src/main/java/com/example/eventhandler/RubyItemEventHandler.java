package com.example.eventhandler;

import com.example.item.ModItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RubyItemEventHandler {

    public static void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            // ตรวจสอบว่าใช้ไอเท็มที่เราสร้างในการขุด
            if (player.getMainHandStack().getItem() == ModItems.RUBY) {

                // destroy main block -> First block which mined
                world.breakBlock(pos, true);

                System.out.printf("current player x %d y %d z %d\n", (int)player.getX(), (int)player.getY(), (int)player.getZ());
                System.out.printf("current block : x %d y %d z %d\n", pos.getX(), pos.getY(), pos.getZ());

                // หันหน้าไปทิศไหน
                 float yaw = player.getYaw();

                 // ก้มหน้าเงยหน้า
                float pitch = player.getPitch();

                //create block position for keep list block which you want to destroy
                List<BlockPos> blockPosList;

                //check case player position and block position
                // > 0 ระยะก้มหน้า < 0 ระยะเงยหน้า
                if (pitch > 0 && pitch < 15 || pitch >= -30 && pitch < 0) { // ระยะการก้มหน้า  เงยหน้า
                    //เช็คว่าผู้เล่นหันหน้าไปทางไหน
                    //             West                         East
                    if ((yaw < -45 && yaw >= -135) || (yaw > 45 && yaw <= 135)) {
                        blockPosList = destroyBlockCasePlayerLookWestOrEast(pos, world);
                    } else { // case north or south
                        blockPosList = destroyBlockCasePlayerLookNorthOrSouth(pos, world);
                    }
                }else {
                    blockPosList = destroyBlockCasePlayerYieldOrLookUp(pos, world);
                }

                // loop destroy block position
                for (BlockPos neighborPos: blockPosList) {
                    // ตรวจสอบไม่ให้ทำลายบล็อกหลักซ้ำ (pos)
                    if (!neighborPos.equals(pos) && world.getBlockState(neighborPos).getBlock() != Blocks.AIR) {
                        world.breakBlock(neighborPos, true);  // true เพื่อให้ดรอปไอเท็มจากบล็อกที่ทำลาย
                    }

                }

            }
        });
    }

    public static List<BlockPos> destroyBlockCasePlayerLookWestOrEast(BlockPos pos, World world) {
        List<BlockPos> blockPosList = new ArrayList<>();

        for (int z = -1; z <= 1; z++) {
            for (int y = -1; y <= 1; y++) {
                BlockPos neighborPos = pos.add(0, y, z);
                blockPosList.add(neighborPos);
            }
        }

        return blockPosList; // return block position which destroy
    }

    public static List<BlockPos> destroyBlockCasePlayerLookNorthOrSouth(BlockPos pos, World world) {
        List<BlockPos> blockPosList = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                BlockPos neighborPos = pos.add(x, y, 0);
                blockPosList.add(neighborPos);
            }
        }
        return blockPosList; // return block position which destroy
    }

    public static List<BlockPos> destroyBlockCasePlayerYieldOrLookUp(BlockPos pos, World world) {
        List<BlockPos> blockPosList = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos neighborPos = pos.add(x, 0, z);
                blockPosList.add(neighborPos);

            }
        }
        return blockPosList; // return block position which destroy
    }


}
