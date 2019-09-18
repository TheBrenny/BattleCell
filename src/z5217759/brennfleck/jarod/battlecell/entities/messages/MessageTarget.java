package z5217759.brennfleck.jarod.battlecell.entities.messages;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;

public class MessageTarget extends BCMessage {
	public MessageTarget(BCEntity sender, BCEntity receiver) {
		super(sender, receiver, BCMessage.Type.TARGET, new Object[] {});
	}
}
