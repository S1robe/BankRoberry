package net.gtacraft.bankrobbery;

import net.citizensnpcs.api.npc.NPC;

public class RobStorageObject {
	public NPC npc;
	public Integer[] tasks;

	public RobStorageObject(NPC npc, Integer[] tasks) {
		this.npc = npc;
		this.tasks = tasks;
	}
}
