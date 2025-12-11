package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.TransferWithPID;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.Rumbler;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.Toggle;


@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();

    private final Shooter shooter = new Shooter();

    private final Intake intake = new Intake();

    private final TransferWithPID transfer = new TransferWithPID();
    private final Toggle drivetrainToggle = new Toggle();
    
    private ElapsedTime runtime;
    private Rumbler rumblerDriver;
    private Rumbler rumblerOperator;

    public void init() {
        this.drivetrain.init(hardwareMap);
        ControlScheme.initDriver(gamepad1);
        ControlScheme.initOperator(gamepad2);

        this.intake.init(hardwareMap);
        this.shooter.init(hardwareMap);
        this.transfer.init(hardwareMap);

        this.runtime = new ElapsedTime();
        this.rumblerDriver = new Rumbler(gamepad1, runtime);
        this.rumblerOperator = new Rumbler(gamepad2, runtime);

    }

    public void doTelemetry(Telemetry telemetry) {
        //this.drivetrain.drivetrainData(telemetry);
        this.shooter.shooterTelem(telemetry);
        //this.transfer.doTelemetry(telemetry);

        //this always goes last in this method:
        telemetry.update();
    }

    @Override
    public void loop() {

        this.drivetrain.tankDrive(
                ControlScheme.RIGHT_SIDE_DRIVE.get(),
                ControlScheme.LEFT_SIDE_DRIVE.get(),
                drivetrainToggle.toggleButton(ControlScheme.DRIVE_SLOW_MODE.get())
        );

        this.intake.run(gamepad1.left_trigger, gamepad1.right_trigger);

        this.shooter.run(
                ControlScheme.SHOOTER_POWER_PLUS.get(),
                ControlScheme.SHOOTER_POWER_MINUS.get()
        );
        this.transfer.goToClear(
                ControlScheme.TRANSFER_CLEAR.get()
        );
        this.transfer.goToTransfer(
                ControlScheme.TRANSFER_TRANSFER.get()
        );
        this.transfer.update();

        this.rumblerDriver.update();
        this.rumblerOperator.update();

        doTelemetry(telemetry);
    }
}