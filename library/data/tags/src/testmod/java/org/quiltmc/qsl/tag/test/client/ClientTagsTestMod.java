/*
 * Copyright 2021 QuiltMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.tag.test.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;
import org.quiltmc.qsl.tag.api.TagRegistry;
import org.quiltmc.qsl.tag.api.TagType;
import org.quiltmc.qsl.tag.test.TagsTestMod;

import java.util.function.Consumer;

public final class ClientTagsTestMod implements ClientModInitializer {
	public static final Tag.Identified<Block> TEST_CLIENT_BLOCK_TAG = TagRegistry.BLOCK.create(
			TagsTestMod.id("client_block_tag"), TagType.CLIENT_ONLY
	);
	public static final Tag.Identified<Biome> TEST_CLIENT_BIOME_TAG = TagRegistry.BIOME.create(
			TagsTestMod.id("client_biome_tag"), TagType.CLIENT_ONLY
	);
	public static final Tag.Identified<Item> TEST_DEFAULT_ITEM_TAG = TagRegistry.ITEM.create(
			TagsTestMod.id("default_item_tag"), TagType.CLIENT_FALLBACK
	);

	private static final Consumer<Text> FEEDBACK_CONSUMER = msg -> MinecraftClient.getInstance().player.sendMessage(msg, false);
	private World lastWorld;

	@Override
	public void onInitializeClient() {
		ResourceLoader.registerBuiltinResourcePack(TagsTestMod.id("defaulted_test_pack"), ResourcePackActivationType.NORMAL);
		ResourceLoader.registerBuiltinResourcePack(TagsTestMod.id("required_test_pack"), ResourcePackActivationType.NORMAL);

		ClientWorldTickEvents.START.register((client, world) -> {
			if (this.lastWorld != world) {
				TagsTestMod.displayTag(TEST_CLIENT_BLOCK_TAG, Registry.BLOCK, FEEDBACK_CONSUMER);
				TagsTestMod.displayTag(TEST_CLIENT_BIOME_TAG, client.world.getRegistryManager().get(Registry.BIOME_KEY),
						FEEDBACK_CONSUMER);
				TagsTestMod.displayTag(TEST_DEFAULT_ITEM_TAG, Registry.ITEM, FEEDBACK_CONSUMER);

				this.lastWorld = world;
			}
		});
	}
}