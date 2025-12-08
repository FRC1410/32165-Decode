package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Util.IDs.CONTROL_HUB_IMU;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;

public class Auto extends LinearOpMode {
    Drivetrain drivetrain = new Drivetrain();
    IMU imu = hardwareMap.get(IMU.class, CONTROL_HUB_IMU);
    double lastAngle = 0;
    private enum Steps {
        DRIVE_FORWARD,
        TURN_RIGHT,
        TURN_LEFT,
        DONE,
        NONE
    }
    Steps step = Steps.DRIVE_FORWARD;

    private float getAngle() {
        return imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.XYX, AngleUnit.DEGREES).firstAngle;
    }

    private double[] calculateSpeeds() {
        double[] speeds;

        switch (this.step) {
            case DRIVE_FORWARD:
                if ((Math.abs(280-this.drivetrain.getWheelEncoderValues()[1]) < 50) && (Math.abs(280-this.drivetrain.getWheelEncoderValues()[1]) <= 50)) {
                    this.step = Steps.DONE;
                    speeds = new double[]{0, 0};
                } else {
                    speeds = new double[]{50, 50};
                }
            case TURN_LEFT:
                if (lastAngle + getAngle() == -90) {
                    this.step = Steps.NONE; // Next step
                    speeds = new double[]{0, 0};
                } else {
                    speeds = new double[]{-10, 10};
                }
            case TURN_RIGHT:
                if (lastAngle + getAngle() == 90) {
                    this.step = Steps.NONE; //Next step
                    speeds = new double[]{0, 0};
                } else {
                    speeds = new double[]{10, -10};
                }
            case DONE:
                speeds = new double[]{0, 0};
            default:
                speeds = new double[]{0, 0};
        }

        return speeds;
    }

    @Override
    public void runOpMode() {
        waitForStart();

        while (opModeIsActive()) {
            double[] speeds = calculateSpeeds();
            this.drivetrain.tankDrive(speeds[0], speeds[1], false);
        }
     }

}
