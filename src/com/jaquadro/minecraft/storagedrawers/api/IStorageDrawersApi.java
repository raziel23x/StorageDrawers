package com.jaquadro.minecraft.storagedrawers.api;

import com.jaquadro.minecraft.storagedrawers.api.registry.IRecipeHandlerRegistry;

public interface IStorageDrawersApi
{
    /**
     * Recipe handlers are used to make custom recipes compatible with compacting drawers.
     */
    IRecipeHandlerRegistry recipeHandlerRegistry ();
}
