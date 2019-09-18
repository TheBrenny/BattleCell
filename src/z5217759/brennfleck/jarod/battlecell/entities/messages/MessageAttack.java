package z5217759.brennfleck.jarod.battlecell.entities.messages;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;

public class MessageAttack extends BCMessage {
	private float damage;

	public MessageAttack(BCEntity sender, BCEntity receiver, float damage) {
		super(sender, receiver, BCMessage.Type.ATTACK, new Object[] {damage});
		this.damage = damage;
	}

	public float getDamage() {
		return damage;
	}
}
