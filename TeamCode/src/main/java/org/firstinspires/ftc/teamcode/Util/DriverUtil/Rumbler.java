package org.firstinspires.ftc.teamcode.Util.DriverUtil;

import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Rumbler {

    private Gamepad gamepad;
    private ElapsedTime runtime;

    private boolean halfTimeRumbled = false;
    private boolean endGameRumbled = false;

    // Match is 2 minutes = 120 seconds
    public static final double MATCH_LENGTH = 120.0;
    public static final double END_GAME_WARNING = MATCH_LENGTH - 15.0; // 105 seconds

    Gamepad.RumbleEffect halfTimeRumbleEffect;
    Gamepad.RumbleEffect endGameRumbleEffect;

    public Rumbler(Gamepad gamepad, ElapsedTime runtime) {
        this.gamepad = gamepad;
        this.runtime = runtime;

        // Left side rumble for half time
        halfTimeRumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 0.0, 500)
                .addStep(0.0, 0.0, 200)
                .addStep(1.0, 0.0, 500)
                .build();

        // Both sides rumble for end game warning
        endGameRumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 500)
                .addStep(0.0, 0.0, 200)
                .addStep(1.0, 1.0, 500)
                .addStep(0.0, 0.0, 200)
                .addStep(1.0, 1.0, 500)
                .build();
    }

    public void update() {
        double elapsedTime = runtime.seconds();

        // Rumble left at half time
        if (elapsedTime >= MATCH_HALF_TIME && !halfTimeRumbled) {
            gamepad.runRumbleEffect(halfTimeRumbleEffect);
            halfTimeRumbled = true;
        }

        // Rumble both sides at 15 seconds left
        if (elapsedTime >= END_GAME_WARNING && !endGameRumbled) {
            gamepad.runRumbleEffect(endGameRumbleEffect);
            endGameRumbled = true;
        }
    }

    public void reset() {
        halfTimeRumbled = false;
        endGameRumbled = false;
    }
}
