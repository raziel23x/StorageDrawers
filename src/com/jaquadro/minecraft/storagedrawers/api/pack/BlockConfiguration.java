package com.jaquadro.minecraft.storagedrawers.api.pack;

import com.jaquadro.minecraft.storagedrawers.block.EnumBasicDrawer;

public enum BlockConfiguration
{
    BasicFull1(BlockType.Drawers, EnumBasicDrawer.FULL1),
    BasicFull2(BlockType.Drawers, EnumBasicDrawer.FULL2),
    BasicFull4(BlockType.Drawers, EnumBasicDrawer.FULL4),
    BasicHalf2(BlockType.Drawers, EnumBasicDrawer.HALF2),
    BasicHalf4(BlockType.Drawers, EnumBasicDrawer.HALF4),

    SortingFull1(BlockType.DrawersSorting, EnumBasicDrawer.FULL1),
    SortingFull2(BlockType.DrawersSorting, EnumBasicDrawer.FULL2),
    SortingFull4(BlockType.DrawersSorting, EnumBasicDrawer.FULL4),
    SortingHalf2(BlockType.DrawersSorting, EnumBasicDrawer.HALF2),
    SortingHalf4(BlockType.DrawersSorting, EnumBasicDrawer.HALF4),

    Trim(BlockType.Trim, null),
    TrimSorting(BlockType.TrimSorting, null);

    private final BlockType type;
    private final EnumBasicDrawer drawer;

    BlockConfiguration (BlockType type, EnumBasicDrawer drawer) {
        this.type = type;
        this.drawer = drawer;
    }

    public BlockType getBlockType () {
        return type;
    }

    public int getDrawerCount () {
        return (drawer != null) ? drawer.getDrawerCount() : 0;
    }

    public boolean isHalfDepth () {
        return (drawer != null) ? drawer.isHalfDepth() : false;
    }

    public static BlockConfiguration by (BlockType type, EnumBasicDrawer drawer) {
        for (BlockConfiguration config : values()) {
            if (config.type == type && config.drawer == drawer)
                return config;
        }

        return null;
    }
}
