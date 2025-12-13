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
    public static Supplier<Boolean> TRANSFER_ACTIVE;

    //Indexer
    public static Supplier<Boolean> INDEXER_NEXT_SHOOTING;  // A - next shooting position (colors)
    public static Supplier<Boolean> INDEXER_NEXT_INTAKE;    // B - next intake position (half positions)

    //Shooter
    public static Supplier<Boolean> SHOOTER_CYCLE;

    public static void initDriver(Gamepad gamepad1) {
        LEFT_SIDE_DRIVE = () -> gamepad1.left_stick_y;
        RIGHT_SIDE_DRIVE = () -> gamepad1.right_stick_y;
        DRIVE_SLOW_MODE = () -> gamepad1.a;
        INTAKE_IN = () -> gamepad1.right_trigger;
        INTAKE_OUT = () -> gamepad1.left_trigger;
        TRANSFER_ACTIVE = () -> gamepad1.left_bumper;
    }

    public static void initOperator(Gamepad gamepad2){
        SHOOTER_CYCLE = () -> gamepad2.rightBumperWasPressed();
        INTAKE_IN = () -> gamepad2.right_trigger;
        INTAKE_OUT = () -> gamepad2.left_trigger;

        INDEXER_NEXT_SHOOTING = () -> gamepad2.aWasPressed();  // A - next shooting position (colors)
        INDEXER_NEXT_INTAKE = () -> gamepad2.bWasPressed();    // B - next intake position (half positions)
    }
}