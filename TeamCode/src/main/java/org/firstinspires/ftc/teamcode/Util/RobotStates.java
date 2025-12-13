package org.firstinspires.ftc.teamcode.Util;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorIMUNonOrthogonal;

public class RobotStates {
    public enum Drivetrain {
        FULL_SPEED,
        HALF_SPEED
    }
    public enum Transfer{
        TRANSFER,
        CLEAR
    }
    public enum Indexer{
        RED,
        RED_BLUE_HALF,   // halfway between RED and BLUE
        BLUE,
        BLUE_WHITE_HALF, // halfway between BLUE and WHITE
        WHITE,
        WHITE_RED_HALF   // halfway between WHITE and RED
    }
    public enum ShooterStates{
        FORWARD,
        BACKWARD,
        NEUTRAL,
        HALF_POWER
    }

}
