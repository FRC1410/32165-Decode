package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;

@Autonomous(name = "Auto", group = "Auto")
public class Auto extends LinearOpMode {
    Drivetrain drivetrain = new Drivetrain();
    ElapsedTime timer = new ElapsedTime();

    private enum Steps {
        DRIVE_FORWARD,
        DONE
    }
    Steps step = Steps.DRIVE_FORWARD;

    @Override
    public void runOpMode() {
        // Initialize drivetrain with hardwareMap
        this.drivetrain.init(hardwareMap);

        waitForStart();

        // Reset timer when OpMode starts
        timer.reset();

        while (opModeIsActive()) {
            switch (step) {
                case DRIVE_FORWARD:
                    if (timer.milliseconds() < 100) {
                        // Drive forward at 50% power
                        drivetrain.tankDrive(0.5, 0.5, false);
                    } else {
                        // Stop and move to DONE state
                        drivetrain.tankDrive(0, 0, false);
                        step = Steps.DONE;
                    }
                    break;

                case DONE:
                    drivetrain.tankDrive(0, 0, false);
                    break;
            }

            telemetry.addData("Step", step);
            telemetry.addData("Time (ms)", timer.milliseconds());
            telemetry.update();
        }
    }
}
