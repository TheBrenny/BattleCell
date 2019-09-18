package z5217759.brennfleck.jarod.battlecell.entities.messages;

import java.util.Objects;

import com.thebrenny.jumg.entities.messaging.Message;
import com.thebrenny.jumg.entities.messaging.MessageListener;

public abstract class BCMessage extends Message {
	private Type type;
	
	public BCMessage(MessageListener<?> sender, MessageListener<?> receiver, Type type, Object[] data) {
		super(sender, receiver, type.id, data);
		this.type = type;
	}
	public BCMessage(MessageListener<?> sender, MessageListener<?> receiver, Type type, Object[] data, long delay) {
		super(sender, receiver, type.id, data, delay);
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.type);
	}
	public String getUniqueString() {
		return "Message" + this.type.name + ":" + this.type.id + "@" + hashCode();
	}
	public String toString() {
		return "Message{name" + this.type.name + ", sender:" + this.sender + ", receiver:" + receiver + ", id:" + id + ", data:" + data + ", timestamp:" + timestamp + ", delay:" + delay + "}";
	}
	
	public static enum Type {
		ATTACK(0, "Attack"), TARGET(1, "Target");
		
		public final int id;
		public final String name;
		
		Type(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}
}
