package org.firstinspires.ftc.teamcode.Subsystem;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static org.firstinspires.ftc.teamcode.Util.Tuning.*;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;
import org.firstinspires.ftc.teamcode.Util.PIDController;

import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.RobotStates.Transfer;

public class TransferWithPID {

    DcMotorEx paddleMotor;
    PIDController transferPID;


    RobotStates.Transfer currentState = RobotStates.Transfer.CLEAR;

    public void init(HardwareMap hardwareMap) {

        this.paddleMotor = hardwareMap.get(DcMotorEx.class, TRANSFER_MOTOR_ID);

        this.paddleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.paddleMotor.setDirection(FORWARD);

        this.paddleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.paddleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.transferPID = new PIDController(TRANSFER_P, TRANSFER_I, TRANSFER_D);
    }

    public RobotStates.Transfer getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(RobotStates.Transfer currentState) {
        if (this.currentState != currentState) {
            this.transferPID.reset();
        }
        this.currentState = currentState;
    }

    public double getTargetPos(RobotStates.Transfer desiredState) {
        switch (desiredState) {
            case CLEAR:
                return PADDLE_POS_1;
            case TRANSFER:
                return PADDLE_POS_2;
            default:
                return PADDLE_POS_1;
        }
    }

    public int getCurrentPos() {
        return this.paddleMotor.getCurrentPosition();
    }

    public double update() {
        RobotStates.Transfer desiredState = this.getCurrentState();
        double targetPos = this.getTargetPos(desiredState);
        double currentPos = this.getCurrentPos();
        double error = targetPos - currentPos;

        if (Math.abs(error) <= TRANSFER_THRESHHOLD) {
            this.paddleMotor.setPower(0);
            return 0;
        }

        double output = this.transferPID.calculate(targetPos, currentPos);
        output = Math.max(-0.5, Math.min(0.5, output));

        this.paddleMotor.setPower(output);
        return output;
    }

    public void goToClear(Boolean go) {
        if(go) {
            this.setCurrentState(RobotStates.Transfer.CLEAR);
        }
    }

    public void goToTransfer(Boolean go) {
        if (go) {
            this.setCurrentState(RobotStates.Transfer.TRANSFER);
        }
    }

    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Transfer State", this.currentState);
        telemetry.addData("Transfer Target", this.getTargetPos(this.currentState));
        telemetry.addData("Transfer Position", this.getCurrentPos());
    }
}
