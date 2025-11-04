package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
@TeleOp
public class ServoTest extends OpMode {
    CRServo leftindex;

    @Override
    public void init() {
        leftindex =  hardwareMap.get(CRServo.class, "leftindex");
    }

    @Override
    public void loop() {
        if (gamepad1.a){
            leftindex.setPower(1);
        }
        else{
            leftindex.setPower(0);
        }

        telemetry.addData("feeder power", leftindex.getPower());
    }
}
