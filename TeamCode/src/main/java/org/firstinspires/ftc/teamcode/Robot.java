package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.Indexer;
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
    private final Indexer indexer = new Indexer();
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
        this.indexer.init(hardwareMap);

        this.runtime = new ElapsedTime();
        this.rumblerDriver = new Rumbler(gamepad1, runtime);
        this.rumblerOperator = new Rumbler(gamepad2, runtime);

    }

    public void doTelemetry() {
        this.drivetrain.drivetrainData(telemetry);
        this.transfer.doTelemetry(telemetry);
        this.indexer.doTelemetry(telemetry);
        this.shooter.shooterTelem(telemetry);

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

        this.intake.run(ControlScheme.INTAKE_IN.get(), ControlScheme.INTAKE_OUT.get());

        this.transfer.goToTransfer(
                ControlScheme.TRANSFER_ACTIVE.get()
        );
        this.transfer.update();

        this.indexer.nextShootingPosition(ControlScheme.INDEXER_NEXT_SHOOTING.get());
        this.indexer.nextIntakePosition(ControlScheme.INDEXER_NEXT_INTAKE.get());
        this.indexer.update();

        this.rumblerDriver.update();
        this.rumblerOperator.update();
        if (ControlScheme.SHOOTER_CYCLE.get()) {
            this.shooter.cycle();
        }

        doTelemetry();
    }
}