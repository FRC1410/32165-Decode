package org.firstinspires.ftc.teamcode.Subsystem;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static org.firstinspires.ftc.teamcode.Util.Tuning.*;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;
import org.firstinspires.ftc.teamcode.Util.PIDController;

import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.RobotStates.Transfer;

public class TransferWithPID {

    DcMotorEx paddleMotor;
    PIDController transferPID;
    ElapsedTime transferTimer;
    boolean waitingToReturnToClear = false;

    RobotStates.Transfer currentState = RobotStates.Transfer.CLEAR;

    public void init(HardwareMap hardwareMap) {

        this.paddleMotor = hardwareMap.get(DcMotorEx.class, TRANSFER_MOTOR_ID);

        this.paddleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.paddleMotor.setDirection(FORWARD);

        this.paddleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.paddleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.transferPID = new PIDController(TRANSFER_P, TRANSFER_I, TRANSFER_D);
        this.transferTimer = new ElapsedTime();
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
                return PADDLE_POS_2;
            case TRANSFER:
                return PADDLE_POS_1;
            default:
                return PADDLE_POS_2;
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

        boolean atTarget = Math.abs(error) <= TRANSFER_THRESHHOLD;

        // If we're in TRANSFER state and at the target position, start the timer
        if (desiredState == RobotStates.Transfer.TRANSFER && atTarget) {
            if (!waitingToReturnToClear) {
                // Just reached transfer position, start timer
                waitingToReturnToClear = true;
                transferTimer.reset();
            } else if (transferTimer.seconds() >= 1.0) {
                // Timer elapsed, go back to CLEAR
                this.setCurrentState(RobotStates.Transfer.CLEAR);
                waitingToReturnToClear = false;
                // Re-fetch state and target after automatic state change
                desiredState = this.getCurrentState();
                targetPos = this.getTargetPos(desiredState);
                error = targetPos - currentPos;
                atTarget = Math.abs(error) <= TRANSFER_THRESHHOLD;
            }
        } else {
            waitingToReturnToClear = false;
        }

        if (atTarget) {
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
