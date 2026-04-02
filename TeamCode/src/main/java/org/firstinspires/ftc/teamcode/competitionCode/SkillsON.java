package org.firstinspires.ftc.teamcode.competitionCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MecanumDrive", group="TeleOp")
public class SkillsON extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    @java.lang.Override
    public void init() {
        // NOTE: Make sure these names match exactly what you have in the Robot Configuration on your Driver Hub
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        // Reverse one side so forward works correctly
        // Usually, the right side is reversed, but this depends on your build
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @java.lang.Override
    public void loop() {
        // Controller inputs
        double y  = -gamepad1.left_stick_y;   // Forward/Back
        double x  = gamepad1.left_stick_x;    // Strafe (Left/Right)
        double rx = gamepad1.right_stick_x;   // Rotate (Turn)

        // Mecanum kinematics formulas
        double fl = y + x + rx;
        double bl = y - x + rx;
        double fr = y - x - rx;
        double br = y + x - rx;

        // Normalize so values stay between -1 and 1
        double max = Math.max(Math.abs(fl),
                Math.max(Math.abs(bl),
                        Math.max(Math.abs(fr), Math.abs(br))));

        if (max > 1.0) {
            fl /= max;
            bl /= max;
            fr /= max;
            br /= max;
        }

        // Set motor power
        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);
    }
}