package me.deftware.client.framework.registry;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemAdapter;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum ItemRegistry implements IRegistry.IdentifiableRegistry<Item, Void> {

	INSTANCE;

	@Override
	public Stream<Item> stream() {
		return BuiltInRegistries.ITEM
				.stream()
				.map(ItemAdapter::wrap);
	}

}
