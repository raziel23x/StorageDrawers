package com.jaquadro.minecraft.storagedrawers.block;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityController;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.jaquadro.minecraft.storagedrawers.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockController extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    /*@SideOnly(Side.CLIENT)
    private IIcon iconFront;
    @SideOnly(Side.CLIENT)
    private IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private IIcon iconSideEtched;
    @SideOnly(Side.CLIENT)
    private IIcon iconTrim;*/

    public BlockController (String name) {
        super(Material.rock);

        setUnlocalizedName(name);
        setCreativeTab(ModCreativeTabs.tabStorageDrawers);
        setHardness(5f);
        setStepSound(Block.soundTypeStone);
        setBlockBounds(0, 0, 0, 1, 1, 1);
        setTickRandomly(true);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public int getRenderType () {
        return StorageDrawers.proxy.controllerRenderID;
    }

    @Override
    public int tickRate (World world) {
        return 100;
    }

    @Override
    public Item getItemDropped (IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.controller);
    }

    @Override
    public void onBlockAdded (World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            Block blockNorth = world.getBlockState(pos.north()).getBlock();
            Block blockSouth = world.getBlockState(pos.south()).getBlock();
            Block blockWest = world.getBlockState(pos.west()).getBlock();
            Block blockEast = world.getBlockState(pos.east()).getBlock();

            EnumFacing facing = (EnumFacing)state.getValue(FACING);

            if (facing == EnumFacing.NORTH && blockNorth.isFullBlock() && !blockSouth.isFullBlock())
                facing = EnumFacing.SOUTH;
            if (facing == EnumFacing.SOUTH && blockSouth.isFullBlock() && !blockNorth.isFullBlock())
                facing = EnumFacing.NORTH;
            if (facing == EnumFacing.WEST && blockWest.isFullBlock() && !blockEast.isFullBlock())
                facing = EnumFacing.EAST;
            if (facing == EnumFacing.EAST && blockEast.isFullBlock() && !blockWest.isFullBlock())
                facing = EnumFacing.WEST;

            world.setBlockState(pos, state.withProperty(FACING, facing), 2);
        }
    }

    @Override
    public IBlockState onBlockPlaced (World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy (World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(FACING, entity.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        EnumFacing blockDir = (EnumFacing)state.getValue(FACING);
        if (blockDir != side)
            return false;

        if (!world.isRemote) {
            TileEntityController te = getTileEntitySafe(world, pos);
            te.interactPutItemsIntoInventory(player);
        }

        return true;
    }

    @Override
    public boolean isSideSolid (IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (side.ordinal() != getTileEntity(world, pos).getDirection())
            return true;

        return false;
    }

    @Override
    public void updateTick (World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote)
            return;

        TileEntityController te = getTileEntity(world, pos);
        if (te == null)
            return;

        te.updateCache();

        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    @Override
    public IBlockState getStateForEntityRender (IBlockState state) {
        return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    @Override
    public IBlockState getStateFromMeta (int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y)
            facing = EnumFacing.NORTH;

        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState (IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockState createBlockState () {
        return new BlockState(this, new IProperty[] { FACING });
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        switch (side) {
            case 0:
            case 1:
                return iconSide;
            case 4:
                return iconFront;
            default:
                return iconSideEtched;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntityController tile = getTileEntity(blockAccess, x, y, z);
        if (tile == null)
            return iconFront;

        if (side == tile.getDirection())
            return iconFront;

        switch (side) {
            case 0:
            case 1:
                return iconSide;
            default:
                return iconSideEtched;
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconTrim (int meta) {
        return iconTrim;
    }*/

    @Override
    public TileEntityController createNewTileEntity (World world, int meta) {
        return new TileEntityController();
    }

    public TileEntityController getTileEntity (IBlockAccess blockAccess, BlockPos pos) {
        TileEntity tile = blockAccess.getTileEntity(pos);
        return (tile instanceof TileEntityController) ? (TileEntityController) tile : null;
    }

    public TileEntityController getTileEntitySafe (World world, BlockPos pos) {
        TileEntityController tile = getTileEntity(world, pos);
        if (tile == null) {
            tile = createNewTileEntity(world, 0);
            world.setTileEntity(pos, tile);
        }

        return tile;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        iconFront = register.registerIcon(StorageDrawers.MOD_ID + ":drawers_controller_front");
        iconSide = register.registerIcon(StorageDrawers.MOD_ID + ":drawers_comp_side");
        iconSideEtched = register.registerIcon(StorageDrawers.MOD_ID + ":drawers_comp_side_2");
        iconTrim = register.registerIcon(StorageDrawers.MOD_ID + ":drawers_comp_trim");
    }*/
}