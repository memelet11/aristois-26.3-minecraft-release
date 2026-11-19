package me.deftware.client.framework.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.WritableBookItem;
import java.util.function.Predicate;

public enum ItemTypes {

    Tool(ofMc(mc -> mc.components().has(DataComponents.TOOL))),
    SplashPotion(ofMc(mc -> mc instanceof SplashPotionItem)),
    Soup(ofMc(mc -> mc == Items.BEETROOT_SOUP || mc == Items.MUSHROOM_STEW || mc == Items.SUSPICIOUS_STEW)),
    WritableBook(ofMc(mc -> mc instanceof WritableBookItem)),
    Bow(ofMc(mc -> mc instanceof BowItem)),
    Crossbow(ofMc(mc -> mc instanceof CrossbowItem)),
    Potion(ofMc(mc -> mc instanceof PotionItem)),
    FishingRod(ofMc(mc -> mc instanceof FishingRodItem)),
    Trident(ofMc(mc -> mc instanceof TridentItem)),
    ShulkerBox(item -> item != null && item.asMinecraftItem() instanceof BlockItem && item.getTranslationKey().contains("shulker_box")),
    RangedWeapon(ofMc(mc -> mc instanceof ProjectileWeaponItem)),
    Throwable(ofMc(mc -> mc instanceof BowItem || mc instanceof CrossbowItem || mc instanceof SnowballItem
            || mc instanceof EggItem || mc instanceof EnderpearlItem || mc instanceof SplashPotionItem
            || mc instanceof LingeringPotionItem || mc instanceof FishingRodItem
            || mc instanceof TridentItem
    ));

    private static Predicate<Item> ofMc(Predicate<net.minecraft.world.item.Item> predicate) {
        return item -> {
            if (item == null) {
                return false;
            }
            net.minecraft.world.item.Item mc = item.asMinecraftItem();
            return mc != null && predicate.test(mc);
        };
    }

    private final Predicate<Item> predicate;

    ItemTypes(Predicate<Item> predicate) {
        this.predicate = predicate;
    }

    public boolean is(Item block) {
        return predicate.test(block);
    }

}
