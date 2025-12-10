package org.firstinspires.ftc.teamcode.Subsystem;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {

    private DcMotorEx motorShooter;


    public void init(HardwareMap hardwareMap) {

        this.motorShooter = hardwareMap.get(DcMotorEx.class, SHOOTER_MOTOR_ID);

        this.motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motorShooter.setDirection(FORWARD);

        this.motorShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void shooterTelem(Telemetry telemetry){
        double p = this.motorShooter.getPower();
        telemetry.addData("Power: ", p);
    }

    public void run(Float firstAxisValue, Float secondAxisValue) {

        this.motorShooter.setPower(firstAxisValue - secondAxisValue);


    }
}
