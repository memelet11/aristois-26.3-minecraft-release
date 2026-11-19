package me.deftware.client.framework.network.packets;

import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class CPacketEntityAction extends PacketWrapper {
    
    public CPacketEntityAction(Packet<?> packet) {
        super(packet);
    }
    
    public Action getAction() {
        return switch (((ServerboundPlayerCommandPacket) packet).getAction()) {
            case STOP_SLEEPING -> Action.STOP_SLEEP;
            case START_SPRINTING -> Action.START_SPRINT;
            case STOP_SPRINTING -> Action.STOP_SPRINT;
            case START_RIDING_JUMP -> Action.START_HORSE_JUMP;
            case STOP_RIDING_JUMP -> Action.STOP_HORSE_JUMP;
            case OPEN_INVENTORY -> Action.OPEN_INVENTORY;
            case START_FALL_FLYING -> Action.START_GLIDING;
        };
    }
    
    public enum Action {
        START_SNEAK, STOP_SNEAK, STOP_SLEEP, START_SPRINT, STOP_SPRINT, START_HORSE_JUMP, STOP_HORSE_JUMP, OPEN_INVENTORY, START_GLIDING
    }
}
