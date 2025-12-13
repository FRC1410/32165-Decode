package org.firstinspires.ftc.teamcode.Subsystem;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ManualIndexer {
    DcMotorEx indexer;

    public void init(HardwareMap hardwareMap) {
        this.indexer = hardwareMap.get(DcMotorEx.class, INTAKE_MOTOR_ID);

        this.indexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.indexer.setDirection(DcMotorEx.Direction.FORWARD);

        this.indexer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.indexer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}

    public void run(double intake, double outtake){
        this.indexer.setPower((outtake) - (intake));
    }
}
