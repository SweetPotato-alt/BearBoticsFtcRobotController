package org.firstinspires.ftc.teamcode.competitionCode.teleop;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name="OneController", group ="TeleOP")
public class OneController extends OpMode {
    DcMotor fleft, fright, rleft, rright, launcher;
    CRServo feeder, plow;
    CRServo leftIndex, rightIndex;
    DistanceSensor distance;
    private DistanceSensor distSensor;
    private Orientation angles;
    private final double PRECISION_SCALE = 0.3;
    boolean parkingMode = false;
    boolean lastParking = false;
    boolean launcherOn = false;
    boolean indexActive = false;
    boolean aPressedLast = false;
    private double targetAngle = 0;
    private boolean holdAngle = false;

    @Override
    public void init() {
        // Drive motors
        fleft  = hardwareMap.get(DcMotor.class, "fleft");
        fright = hardwareMap.get(DcMotor.class, "fright");
        rleft  = hardwareMap.get(DcMotor.class, "rleft");
        rright = hardwareMap.get(DcMotor.class, "rright");

        // Typical mecanum wiring: left side forward, right side reversed
        fleft.setDirection(DcMotorSimple.Direction.FORWARD);
        rleft.setDirection(DcMotorSimple.Direction.FORWARD);
        fright.setDirection(DcMotorSimple.Direction.REVERSE);
        rright.setDirection(DcMotorSimple.Direction.REVERSE);

        // Launcher
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        launcher.setDirection(DcMotorSimple.Direction.FORWARD);
        launcher.setPower(0);

        // Feeder
        feeder = hardwareMap.get(CRServo.class, "feeder");
        feeder.setPower(0);

        // Index
        leftIndex  = hardwareMap.get(CRServo.class, "leftindex");
        rightIndex = hardwareMap.get(CRServo.class, "rightindex");
        leftIndex.setPower(0);
        rightIndex.setPower(0);

        // Sensors
        distance = hardwareMap.get(DistanceSensor.class, "distanceSensor");

        //Plow
        plow = hardwareMap.get(CRServo.class, "plow");
        plow.setPower(0);

    }

    @Override
    public void loop() {
        // Sensors
        double dist = distance.getDistance(DistanceUnit.CM);

        // --- Mecanum Drive ---
        // left_stick_y  = forward/back
        // left_stick_x  = strafe left/right
        // right_stick_x = rotate
        float drive  = -gamepad1.left_stick_y;   // negate: stick up = positive
        float strafe =  gamepad1.left_stick_x;
        float turn   =  gamepad1.right_stick_x;

        // Deadzone
        if (Math.abs(drive)  < 0.05) drive  = 0;
        if (Math.abs(strafe) < 0.05) strafe = 0;
        if (Math.abs(turn)   < 0.05) turn   = 0;

        // Mecanum power mix
        float flPower = drive + strafe + turn;
        float frPower = drive - strafe - turn;
        float rlPower = drive - strafe + turn;
        float rrPower = drive + strafe - turn;

        // Normalize so no value exceeds 1.0
        float max = Math.max(1.0f, Math.max(
                Math.max(Math.abs(flPower), Math.abs(frPower)),
                Math.max(Math.abs(rlPower), Math.abs(rrPower))));
        flPower /= max;
        frPower /= max;
        rlPower /= max;
        rrPower /= max;

        // Precision / parking mode toggle (touchpad)
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

        fleft.setPower(flPower  * 0.9);
        fright.setPower(frPower * 0.9);
        rleft.setPower(rlPower  * 0.9);
        rright.setPower(rrPower * 0.9);

        // --- Launcher toggle (A button) ---
        if (gamepad1.a && !aPressedLast) {
            launcherOn = !launcherOn;
            launcher.setPower(launcherOn ? 1.0 : 0.0);
        }
        aPressedLast = gamepad1.a;

        // --- Feeder () ---


        // --- Index (right bumper) ---
        if (gamepad1.right_bumper) {
            leftIndex.setPower(-1.0);
            rightIndex.setPower(1.0);
        } else {
            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);
        }

        // plow
        if (gamepad1.x) {
            plow.setPower(0.5);
        } else if (gamepad1.b) {
            plow.setPower(-0.5);
        }


        // --- Telemetry ---
        telemetry.addData("FL / FR", "%.2f / %.2f", flPower, frPower);
        telemetry.addData("RL / RR", "%.2f / %.2f", rlPower, rrPower);
        telemetry.addData("Drive Mode", parkingMode ? "Precision" : "Full");
        telemetry.addData("Launcher",   launcherOn  ? "ON"        : "OFF");
        telemetry.addData("Index",      indexActive ? "SPINNING"  : "STOPPED");
        telemetry.addData("Distance",   "%.2f cm",  dist);
        telemetry.update();
    }
}