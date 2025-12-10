package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name="IMU Angle Hold Test", group = "Testing")
public class IMUAngleHoldTest extends LinearOpMode {

    //Hardware
    BNO055IMU imu;

    //Angle hold variables
    double targetAngle = 0;
    boolean angleHoldEnabled = false;

    // IMU Setup
    private void initIMU (){

        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(params);

        telemetry.addLine("IMU Initialized");
        telemetry.update();
    }

    //MAIN Program
    @Override
    public void runOpMode(){
        initIMU();
        waitForStart();

        while (opModeIsActive()){

            Orientation orientation = imu.getAngularOrientation(
                    AxesReference.INTRINSIC,
                    AxesOrder.ZYX,
                    AngleUnit.DEGREES
            );

            double currentAngle = orientation.firstAngle;

            telemetry.addData("Angle", currentAngle);
            telemetry.addData("Target", targetAngle);
            telemetry.addData("Angle Hold", angleHoldEnabled);
            telemetry.update();

        }
    }

}

