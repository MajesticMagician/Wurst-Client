/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Automatically leaves the server when your health is low.\n"
		+ "Type `.leave mode chars` to make it bypass CombatLogger.",
	name = "AutoLeave")
public class AutoLeaveMod extends Mod implements UpdateListener
{
	@Override
	public String getRenderName()
	{
		String name = getName() + "[";
		switch(wurst.options.autoLeaveMode)
		{
			case 0:
				name += "Quit";
				break;
			case 1:
				name += "Chars";
				break;
			default:
				break;
		}
		name += "]";
		return name;
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(mc.thePlayer.getHealth() <= 8.0
			&& !mc.thePlayer.capabilities.isCreativeMode
			&& (!mc.isIntegratedServerRunning() || Minecraft.getMinecraft().thePlayer.sendQueue
				.getPlayerInfo().size() > 1))
		{
			switch(wurst.options.autoLeaveMode)
			{
				case 0:
					mc.theWorld.sendQuittingDisconnectingPacket();
					break;
				case 1:
					mc.thePlayer.sendQueue
						.addToSendQueue(new C01PacketChatMessage("�"));
					break;
				default:
					break;
			}
			setEnabled(false);
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
