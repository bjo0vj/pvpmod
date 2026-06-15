/*
 * STUB - placeholder for compilation.
 */

package meteordevelopment.meteorclient.systems.modules.movement.elytrafly;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;

public class ElytraFly extends Module {
    public final Setting<ElytraFlightModes> flightMode = settings.getDefaultGroup().add(new EnumSetting.Builder<ElytraFlightModes>()
        .name("mode")
        .defaultValue(ElytraFlightModes.Vanilla)
        .build()
    );

    public ElytraFly() {
        super(Categories.Movement, "elytrafly", "Stub");
    }

    public boolean canPacketEfly() {
        return false;
    }
}
