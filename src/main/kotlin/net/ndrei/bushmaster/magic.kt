package net.ndrei.bushmaster

import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.FakePlayer

fun Class<*>.couldBe(thing: String): Boolean =
    if (this.name == thing) { true }
    else this.superclass?.couldBe(thing) ?: false

fun IBlockState.isIntPropertyMax(propertyName: String): Boolean {
    val property = this.propertyKeys
        .filterIsInstance<PropertyInteger>()
        .firstOrNull { it.name == propertyName } ?: return false

    return (this.getValue(property) == property.allowedValues.max())
}

fun IBlockState.testBoolProperty(propertyName: String): Boolean {
    val property = this.propertyKeys
        .filterIsInstance<PropertyBool>()
        .firstOrNull { it.name == propertyName } ?: return false

    return this.getValue(property)
}

fun BlockPos.harvest(loot: MutableList<ItemStack>, world: World, radius: Int) {
    val aabb = AxisAlignedBB(this.west(radius).north(radius).down(radius), this.east(radius).south(radius).up(radius))

    world.getEntitiesWithinAABB(EntityItem::class.java, aabb).mapTo(loot) {
        world.removeEntity(it)
        it.item
    }
}

fun FakePlayer.loot(loot: MutableList<ItemStack>) {
    for(index in 0..this.inventory.sizeInventory) {
        val stack = this.inventory.getStackInSlot(index)
        if (!stack.isEmpty) {
            loot.add(stack)
        }
    }
    this.inventory.clear()
}
