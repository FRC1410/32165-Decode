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
            case RED_BLUE_HALF:
                return INDEXER_POS_RED_BLUE_HALF;
            case BLUE_WHITE_HALF:
                return INDEXER_POS_BLUE_WHITE_HALF;
            case WHITE_RED_HALF:
                return INDEXER_POS_WHITE_RED_HALF;
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

    // Intake positions only (halfway positions)
    private static final RobotStates.Indexer[] INTAKE_STATES = {
        RobotStates.Indexer.RED_BLUE_HALF,
        RobotStates.Indexer.BLUE_WHITE_HALF,
        RobotStates.Indexer.WHITE_RED_HALF
    };

    // Shooting positions only (colors)
    private static final RobotStates.Indexer[] SHOOTING_STATES = {
        RobotStates.Indexer.RED,
        RobotStates.Indexer.BLUE,
        RobotStates.Indexer.WHITE
    };

    // A button - go to next shooting position (colors only)
    public void nextShootingPosition(Boolean go) {
        if (go) {
            int currentIndex = getShootingIndex();
            int nextIndex = (currentIndex + 1) % SHOOTING_STATES.length;
            this.setCurrentState(SHOOTING_STATES[nextIndex]);
        }
    }

    // B button - go to next indexer position (halfway positions only)
    public void nextIntakePosition(Boolean go) {
        if (go) {
            int currentIndex = getIntakeIndex();
            int nextIndex = (currentIndex + 1) % INTAKE_STATES.length;
            this.setCurrentState(INTAKE_STATES[nextIndex]);
        }
    }

    private int getShootingIndex() {
        // Find nearest shooting position
        switch (this.currentState) {
            case RED:
            case RED_BLUE_HALF:
            case WHITE_RED_HALF:
                return 0; // RED
            case BLUE:
            case BLUE_WHITE_HALF:
                return 1; // BLUE
            case WHITE:
            default:
                return 2; // WHITE
        }
    }

    private int getIntakeIndex() {
        // Find nearest indexer (half) position
        switch (this.currentState) {
            case RED_BLUE_HALF:
            case RED:
            case BLUE:
                return 0; // RED_BLUE_HALF
            case BLUE_WHITE_HALF:
            case WHITE:
                return 1; // BLUE_WHITE_HALF
            case WHITE_RED_HALF:
            default:
                return 2; // WHITE_RED_HALF
        }
    }

    public void doTelemetry(Telemetry telemetry) {
        telemetry.addData("Indexer State", this.currentState);
        telemetry.addData("Indexer Target", this.getTargetPos(this.currentState));
        telemetry.addData("Indexer Position", this.getCurrentPos());
    }
}
