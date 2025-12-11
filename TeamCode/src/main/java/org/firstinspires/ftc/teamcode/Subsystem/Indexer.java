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

public class Indexer {
    DcMotorEx indexerMotor;
    PIDController indexerPID;
    ElapsedTime indexerTimer;

    RobotStates.Indexer currentState = RobotStates.Indexer.WHITE;

    public void init(HardwareMap hardwareMap) {

        this.indexerMotor = hardwareMap.get(DcMotorEx.class, INDEXER_MOTOR_ID);

        this.indexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.indexerMotor.setDirection(FORWARD);

        this.indexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.indexerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.indexerPID = new PIDController(INDEXER_P, INDEXER_I, INDEXER_D);
        this.indexerTimer = new ElapsedTime();
    }

    public RobotStates.Indexer getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(RobotStates.Indexer currentState) {
        if (this.currentState != currentState) {
            this.indexerPID.reset();
        }
        this.currentState = currentState;
    }

    public double getTargetPos(RobotStates.Indexer desiredState) {
        switch (desiredState) {
            case RED:
                return INDEXER_POS_RED;
            case BLUE:
                return INDEXER_POS_BLUE;
            case WHITE:
                return INDEXER_POS_WHITE;
            default:
                return INDEXER_POS_WHITE;
        }
    }

    public int getCurrentPos() {
        return this.indexerMotor.getCurrentPosition();
    }

    public double update() {
        RobotStates.Indexer desiredState = this.getCurrentState();
        double targetPos = this.getTargetPos(desiredState);
        double currentPos = this.getCurrentPos();
        double error = targetPos - currentPos;

        boolean atTarget = Math.abs(error) <= INDEXER_THRESHHOLD;

        if (atTarget) {
            this.indexerMotor.setPower(0);
            return 0;
        }

        double output = this.indexerPID.calculate(targetPos, currentPos);
        output = Math.max(-0.5, Math.min(0.5, output));

        this.indexerMotor.setPower(output);
        return output;
    }

    // Face button A - go to RED state
    public void goToRed(Boolean go) {
        if (go) {
            this.setCurrentState(RobotStates.Indexer.RED);
        }
    }

    // Face button B - go to BLUE state
    public void goToBlue(Boolean go) {
        if (go) {
            this.setCurrentState(RobotStates.Indexer.BLUE);
        }
    }

    // Face button X - go to WHITE state
    public void goToWhite(Boolean go) {
        if (go) {
            this.setCurrentState(RobotStates.Indexer.WHITE);
        }
    }

    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Indexer State", this.currentState);
        telemetry.addData("Indexer Target", this.getTargetPos(this.currentState));
        telemetry.addData("Indexer Position", this.getCurrentPos());
    }
}
