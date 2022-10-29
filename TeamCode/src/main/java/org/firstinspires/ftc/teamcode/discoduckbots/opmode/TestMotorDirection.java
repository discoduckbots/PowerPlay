/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.discoduckbots.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.discoduckbots.hardware.HardwareStore;
import org.firstinspires.ftc.teamcode.discoduckbots.hardware.MecanumDrivetrain;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Test motor direction", group="Linear Opmode")
public class TestMotorDirection extends LinearOpMode {

    //  private static final double THROTTLE = 0.45;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private MecanumDrivetrain mecanumDrivetrain = null;
    /* private Intake intake = null;
    private Shooter shooter = null;
    private WobbleMover wobbleMover = null;
    private TouchSensor touchSensor = null;
    private ColorSensor colorSensor = null; */
    private static final double AUTONOMOUS_SPEED = 0.4;
    @Override
    public void runOpMode() {
        HardwareStore hardwareStore = new HardwareStore(hardwareMap, telemetry, this);
        mecanumDrivetrain = hardwareStore.getMecanumDrivetrain();
      /*   intake = hardwareStore.getIntake();
        shooter = hardwareStore.getShooter();
        wobbleMover = hardwareStore.getWobbleMover();
        touchSensor = hardwareStore.getTouchSensor();
        colorSensor = hardwareStore.getColorSensor(); */

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        int brPos = hardwareStore.backRight.getCurrentPosition();
        int frPos = hardwareStore.frontRight.getCurrentPosition();
        int blPos = hardwareStore.backLeft.getCurrentPosition();
        int flPos = hardwareStore.frontLeft.getCurrentPosition();
        hardwareStore.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardwareStore.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardwareStore.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardwareStore.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardwareStore.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardwareStore.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardwareStore.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardwareStore.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (opModeIsActive()) {
            double[] angles = hardwareStore.getImu().printAngles();
            telemetry.addData("IMU 1:" + angles[0]+ " 2:" + angles[1] + " 3:" + angles[2], "");
            telemetry.addData("br Pos change: " + hardwareStore.backRight.getDirection() + " " ,
                    hardwareStore.backRight.getCurrentPosition() - brPos);
            telemetry.addData("fr Pos change: " + hardwareStore.frontRight.getDirection() + " ",
                    hardwareStore.frontRight.getCurrentPosition() - frPos);
            telemetry.addData("bl Pos change: " + hardwareStore.backLeft.getDirection() + " ",
                    hardwareStore.backLeft.getCurrentPosition() - blPos);
            telemetry.addData("fl Pos change: " + hardwareStore.frontLeft.getDirection() + " ",
                    hardwareStore.frontLeft.getCurrentPosition() - flPos);
            telemetry.update();

            if (gamepad1.a) {
                telemetry.addData("Presing a", "");
                telemetry.update();
                //hardwareStore.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.backLeft.setPower(AUTONOMOUS_SPEED);
            }else {
                hardwareStore.backLeft.setPower(0);
            }

            if (gamepad1.y) {
               // hardwareStore.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.backRight.setPower(AUTONOMOUS_SPEED);
            }else {
                hardwareStore.backRight.setPower(0);
            }
            if (gamepad1.b) {
               // hardwareStore.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.frontLeft.setPower(AUTONOMOUS_SPEED);
            }else {
                hardwareStore.frontLeft.setPower(0);
            }

            if (gamepad1.x) {

               // hardwareStore.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.frontRight.setPower(AUTONOMOUS_SPEED);
            }else {
                hardwareStore.frontRight.setPower(0);
            }
        }








           /* if (gamepad1.b){
                wobbleMover.grabAndLiftByEncoder(6250, this);
            }

            while ( touchSensor.isPressed()) {
                intake.intake();
            }

            colorSensor.blue();
            colorSensor.red();
            colorSensor.green();

            telemetry.addData("Red", colorSensor.red());
            telemetry.addData("BLue", colorSensor.blue());
            telemetry.addData("Green", colorSensor.green());
            telemetry.update();
        } */

        telemetry.addData("MecanumDrivetrainTeleOp", "Stopping");

        shutDown();
    }

    private void shutDown(){
        mecanumDrivetrain.stop();
        //intake.stop();
        //shooter.stop();
    }
}
