package org.firstinspires.ftc.teamcode.Util.DriverUtil;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.Supplier;

public class ControlScheme {

    //Drivetrain
    public static Supplier<Float> LEFT_SIDE_DRIVE;
    public static Supplier<Float> RIGHT_SIDE_DRIVE;
    public static Supplier<Boolean> DRIVE_SLOW_MODE;

    //Shooter
    public static Supplier<Float> SHOOTER_POWER_PLUS;
    public static Supplier<Float> SHOOTER_POWER_MINUS;

    //Intake
    public static Supplier<Float> INTAKE_IN; ;
    public static Supplier<Float> INTAKE_OUT;

    //Transfer Paddle
    public static Supplier<Float> TRANSFER_IN;
    public static Supplier<Float> TRANSFER_OUT;

    public static Supplier<Boolean> TRANSFER_CLEAR;
    public static Supplier<Boolean> TRANSFER_TRANSFER;

    //Indexer
    public static Supplier<Boolean> INDEXER_RED;
    public static Supplier<Boolean> INDEXER_BLUE;
    public static Supplier<Boolean> INDEXER_WHITE;

    public static void initDriver(Gamepad gamepad1) {
        LEFT_SIDE_DRIVE = () -> gamepad1.left_stick_y;
        RIGHT_SIDE_DRIVE = () -> gamepad1.right_stick_y;
        DRIVE_SLOW_MODE = () -> gamepad1.a;
        INTAKE_IN = () -> gamepad1.right_trigger;
        INTAKE_OUT = () -> gamepad1.left_trigger;
    }

    public static void initOperator(Gamepad gamepad2){
        SHOOTER_POWER_PLUS = () -> gamepad2.right_trigger;
        SHOOTER_POWER_MINUS = () -> gamepad2.left_trigger;
        TRANSFER_IN = () -> gamepad2.right_stick_y > 0 ? gamepad2.right_stick_y : 0;
        TRANSFER_OUT = () -> gamepad2.right_stick_y < 0 ? -gamepad2.right_stick_y : 0;
        TRANSFER_CLEAR = () -> gamepad2.right_bumper;
        TRANSFER_TRANSFER = () -> gamepad2.left_bumper;

        INDEXER_RED = () -> gamepad2.a;
        INDEXER_BLUE = () -> gamepad2.b;
        INDEXER_WHITE = () -> gamepad2.x;
    }
}