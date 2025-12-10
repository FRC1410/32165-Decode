package org.firstinspires.ftc.teamcode.Subsystem;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static org.firstinspires.ftc.teamcode.Util.IDs.SHOOTER_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.TRANSFER_MOTOR_ID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Transfer {

    private DcMotorEx motorTransfer;


    public void init(HardwareMap hardwareMap) {

        this.motorTransfer = hardwareMap.get(DcMotorEx.class, TRANSFER_MOTOR_ID);

        this.motorTransfer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motorTransfer.setDirection(FORWARD);

        this.motorTransfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorTransfer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void transferTelem(Telemetry telemetry){
        double p = this.motorTransfer.getPower();
        telemetry.addData("Power: ", p);
    }

    public void run(Float firstAxisValue, Float secondAxisValue) {

        this.motorTransfer.setPower(firstAxisValue - secondAxisValue);


    }
}
