package org.firstinspires.ftc.teamcode.discoduckbots.opmode.poc;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Test Op", group = "Sensor")
@Disabled
public class TestOp extends LinearOpMode {
   private  DcMotorEx intakeMotor;
   private DcMotorEx shooterMotor;
   private Servo pusherServo;


    @Override
    public void runOpMode() throws InterruptedException {

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        pusherServo = hardwareMap.get(Servo.class, "pusher");




        waitForStart();

        while (opModeIsActive()){




        }
    }
}
