package org.firstinspires.ftc.teamcode.competitionCode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name="TwoController", group ="TeleOP")
public class TwoController extends OpMode {

    DcMotor fleft, fright, rleft, rright, launcher;
    CRServo feeder, plow;
    CRServo leftIndex, rightIndex;
    DistanceSensor distanceSensor;

    private Orientation angles;
    private final double PRECISION_SCALE = 0.3;

    boolean parkingMode = false;
    boolean lastParking = false;
    boolean launcherOn = false;
    boolean indexActive = false;

    boolean aPressedLast = false;

    String plowState = "STOPPED";

    @Override
    public void init() {

        // Drive motors
        fleft  = hardwareMap.get(DcMotor.class, "fleft");
        fright = hardwareMap.get(DcMotor.class, "fright");
        rleft  = hardwareMap.get(DcMotor.class, "rleft");
        rright = hardwareMap.get(DcMotor.class, "rright");

        fleft.setDirection(DcMotorSimple.Direction.FORWARD);
        rleft.setDirection(DcMotorSimple.Direction.REVERSE);
        fright.setDirection(DcMotorSimple.Direction.REVERSE);
        rright.setDirection(DcMotorSimple.Direction.REVERSE);

        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Launcher
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        launcher.setDirection(DcMotorSimple.Direction.FORWARD);
        launcher.setPower(0);

        // Feeder
        feeder = hardwareMap.get(CRServo.class, "feeder");
        feeder.setPower(0);

        // Index
        leftIndex = hardwareMap.get(CRServo.class, "leftindex");
        rightIndex = hardwareMap.get(CRServo.class, "rightindex");
        leftIndex.setPower(0);
        rightIndex.setPower(0);

        // Plow
        plow = hardwareMap.get(CRServo.class, "plow");
        plow.setDirection(DcMotorSimple.Direction.FORWARD);
        plow.setPower(0);

        // Distance sensor (ADDED BACK)
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distanceSensor");
    }

    @Override
    public void loop() {

        // Distance sensor reading
        double dist = distanceSensor.getDistance(DistanceUnit.CM);

        // =========================
        // GAMEPAD 1 DRIVE
        // =========================

        float drive = -gamepad1.left_stick_y;
        float strafe = gamepad1.left_stick_x;
        float turn = gamepad1.right_stick_x;

        if (Math.abs(drive) < 0.05) drive = 0;
        if (Math.abs(strafe) < 0.05) strafe = 0;
        if (Math.abs(turn) < 0.05) turn = 0;

        float flPower = drive + strafe + turn;
        float frPower = drive - strafe - turn;
        float rlPower = drive - strafe + turn;
        float rrPower = drive + strafe - turn;

        float max = Math.max(
                1.0f,
                Math.max(
                        Math.max(Math.abs(flPower), Math.abs(frPower)),
                        Math.max(Math.abs(rlPower), Math.abs(rrPower))
                )
        );

        flPower /= max;
        frPower /= max;
        rlPower /= max;
        rrPower /= max;

        boolean currentParking = gamepad1.touchpad;
        if (currentParking && !lastParking) {
            parkingMode = !parkingMode;
        }
        lastParking = currentParking;

        if (parkingMode) {
            flPower *= PRECISION_SCALE;
            frPower *= PRECISION_SCALE;
            rlPower *= PRECISION_SCALE;
            rrPower *= PRECISION_SCALE;
        }

        fleft.setPower(flPower * 1.0);
        fright.setPower(frPower * 1.0);
        rleft.setPower(rlPower * 1.0);
        rright.setPower(rrPower * 1.0);

        // =========================
        // GAMEPAD 2 SYSTEMS
        // =========================

        if (gamepad2.a && !aPressedLast) {
            launcherOn = !launcherOn;
            launcher.setPower(launcherOn ? 1.0 : 0.0);
        }
        aPressedLast = gamepad2.a;

        float feederInput = -gamepad2.left_stick_y;
        if (Math.abs(feederInput) < 0.1) feederInput = 0;
        feeder.setPower(feederInput);

        if (gamepad2.right_bumper) {
            leftIndex.setPower(1.0);
            rightIndex.setPower(-1.0);
            indexActive = true;
        } else {
            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);
            indexActive = false;
        }

        if (gamepad1.x) {
            plow.setPower(0.1);
            plowState = "OPENING";
        } else if (gamepad1.b) {
            plow.setPower(-0.1);
            plowState = "CLOSING";
        } else {
            plow.setPower(0.0);
            plowState = "STOPPED";
        }

        // =========================
        // TELEMETRY
        // =========================

        telemetry.addData("FL / FR", "%.2f / %.2f", flPower, frPower);
        telemetry.addData("RL / RR", "%.2f / %.2f", rlPower, rrPower);
        telemetry.addData("Mode", parkingMode ? "Precision" : "Full");

        telemetry.addData("Launcher", launcherOn ? "ON" : "OFF");
        telemetry.addData("Index", indexActive ? "SPINNING" : "STOPPED");
        telemetry.addData("Plow", plowState);

        telemetry.addData("Distance (cm)", "%.2f", dist);

        telemetry.update();
    }
}