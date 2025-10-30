//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//
//@TeleOp
//public class RevRobotProtoCodeDONOTUSE extends OpMode {
//
//    DcMotor left, right, arm, launcher;
//
//    // --- For launcher toggle ---
//    boolean launcherOn = false;
//    boolean bPressedLast = false;
//
//    // --- For arm control ---
//    boolean aPressedLast = false;
//    final double ARM_TICKS_PER_REV = 1120;  // adjust for your specific motor (e.g., NeverRest 40)
//    final double QUARTER_TURN_TICKS = ARM_TICKS_PER_REV / 4.0;
//
//    @Override
//    public void init() {
//        // Initialize drive motors
//        left = hardwareMap.get(DcMotor.class, "left");
//        left.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        right = hardwareMap.get(DcMotor.class, "right");
//        right.setDirection(DcMotorSimple.Direction.FORWARD);
//
//        // Initialize arm motor
//        arm = hardwareMap.get(DcMotor.class, "arm");
//        arm.setDirection(DcMotorSimple.Direction.FORWARD);
//        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        // Initialize launcher motor
//        launcher = hardwareMap.get(DcMotor.class, "launcher");
//        launcher.setDirection(DcMotorSimple.Direction.FORWARD);
//        launcher.setPower(0);
//
//        telemetry.addLine("Initialized successfully!");
//        telemetry.update();
//    }
//
//    @Override
//    public void loop() {
//
//        // --- DRIVE CONTROL (tank style joystick) ---
//        float drive = gamepad1.left_stick_y;   // forward/back
//        float turn = -gamepad1.left_stick_x;   // left stick controls turn
//
//        float leftPower = drive + turn;
//        float rightPower = drive - turn;
//
//        // Clamp power range
//        leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
//        rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));
//
//        left.setPower(leftPower);
//        right.setPower(rightPower);
//
//        // --- ARM CONTROL (¼ turn per button press) ---
//        if (gamepad1.a && !aPressedLast) {
//            int currentPosition = arm.getCurrentPosition();
//            int targetPosition = currentPosition + (int) QUARTER_TURN_TICKS;
//            arm.setTargetPosition(targetPosition);
//            arm.setPower(0.5);
//        }
//
//        // --- LAUNCHER CONTROL (toggle on/off with button B) ---
//        if (gamepad1.b && !bPressedLast) {
//            launcherOn = !launcherOn; // toggle
//            launcher.setPower(launcherOn ? 1.0 : 0.0);
//        }
//
//        // --- Update previous button states ---
//        aPressedLast = gamepad1.a;
//        bPressedLast = gamepad1.b;
//
//        // --- TELEMETRY ---
//        telemetry.addData("Left Power", leftPower);
//        telemetry.addData("Right Power", rightPower);
//        telemetry.addData("Arm Position", arm.getCurrentPosition());
//        telemetry.addData("Launcher Power", launcher.getPower());
//        telemetry.addData("Launcher On?", launcherOn);
//        telemetry.update();
//    }
//}
