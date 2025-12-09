package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;


@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();

    private final Shooter shooter = new Shooter();

    private final Intake intake = new Intake();
    private final Toggle drivetrainToggle = new Toggle();
    
    public void init() {
        this.drivetrain.init(hardwareMap);

        this.shooter.init(hardwareMap);
    }

    @Override
    public void loop() {

        this.drivetrain.drivetrainData(telemetry);

        this.drivetrain.tankDrive(
                ControlScheme.RIGHT_SIDE_DRIVE.get(),
                ControlScheme.LEFT_SIDE_DRIVE.get(),
                drivetrainToggle.toggleButton(ControlScheme.DRIVE_SLOW_MODE.get())
        );

        this.intake.run(gamepad1.left_trigger, gamepad1.right_trigger);
        this.shooter.run(gamepad1.right_trigger, gamepad1.left_trigger);
    }
}