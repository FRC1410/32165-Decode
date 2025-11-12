package org.firstinspires.ftc.teamcode.Util.DriverUtil;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.Supplier;

public class ControlScheme {

    //Drivetrain
    public static Supplier<Float> LEFT_SIDE_DRIVE;
    public static Supplier<Float> RIGHT_SIDE_DRIVE;
    public static Supplier<Boolean> DRIVE_SLOW_MODE;


    public static void init(Gamepad gamepad1) {
        LEFT_SIDE_DRIVE = () -> gamepad1.left_stick_y;
        RIGHT_SIDE_DRIVE = () -> gamepad1.right_stick_y;
        DRIVE_SLOW_MODE = () -> gamepad1.a;
    }
}