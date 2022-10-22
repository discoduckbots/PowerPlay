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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.discoduckbots.hardware.ConeArm;
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

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Test mc OpMode", group="Linear Opmode")
public class TestTeleOp extends LinearOpMode {

  //  private static final double THROTTLE = 0.45;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private MecanumDrivetrain mecanumDrivetrain = null;
    /* private Intake intake = null;
    private Shooter shooter = null;
    private WobbleMover wobbleMover = null;
    private TouchSensor touchSensor = null;
    private ColorSensor colorSensor = null; */
    private static final double AUTONOMOUS_SPEED = 0.6;
   private static final int LEVEL_1 = 3176;
   private static final int LEVEL_2 = 2111;
   private static final int LEVEL_3 = 1406;
    @Override
    public void runOpMode() {
        HardwareStore hardwareStore = new HardwareStore(hardwareMap, telemetry, this);
        mecanumDrivetrain = hardwareStore.getMecanumDrivetrain();
        ConeArm cargoGrabber = hardwareStore.getCargoGrabber();
      /*   intake = hardwareStore.getIntake();
        shooter = hardwareStore.getShooter();
        wobbleMover = hardwareStore.getWobbleMover();
        touchSensor = hardwareStore.getTouchSensor();
        colorSensor = hardwareStore.getColorSensor(); */

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //DuckDetector duckDetector = new DuckDetector(hardwareStore.getDistanceSensor2());
        while (opModeIsActive()) {
            mecanumDrivetrain.print();
           // cargoGrabber.print(telemetry);

            //duckDetector.isDuckPresent(2);
            /*
            if (gamepad2.dpad_down) {
                cargoGrabber.lift(0.3);
            }
            else if(gamepad2.dpad_up) {
                cargoGrabber.lower(0.3);
            }
            else{
                cargoGrabber.stop();
            }*/
            if (gamepad2.dpad_down) {

                cargoGrabber.liftByEncoder(LEVEL_1);

            } else if (gamepad2.dpad_up) {
                cargoGrabber.liftByEncoder(LEVEL_2);
            } else if (gamepad2.dpad_right) {
                cargoGrabber.liftByEncoder(LEVEL_3);
            }

            if (!gamepad1.a && !gamepad1.b && !gamepad1.y && !gamepad1.x ) {
                mecanumDrivetrain.stop();
                continue;
            }
            if (gamepad1.a) {
               /* hardwareStore.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                hardwareStore.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardwareStore.backLeft.setPower(AUTONOMOUS_SPEED);
                double time = System.nanoTime();
                while (System.nanoTime() - time < 3000000000.0) {
                    //
                }
                telemetry.addData("BL Encoder: " , hardwareStore.backLeft.getCurrentPosition());
                telemetry.update();
                */
                //mecanumDrivetrain.backwardByTime(this,AUTONOMOUS_SPEED,0.5);
                //mecanumDrivetrain.driveByDistance(14, MecanumDrivetrain.DIRECTION_REVERSE, AUTONOMOUS_SPEED);
                mecanumDrivetrain.driveByGyro(15, MecanumDrivetrain.DIRECTION_REVERSE, AUTONOMOUS_SPEED, 0);
            }else {
                //hardwareStore.backLeft.setPower(0);
            }

            if (gamepad1.y) {
                /*hardwareStore.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                hardwareStore.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardwareStore.backRight.setPower(AUTONOMOUS_SPEED);
                double time = System.nanoTime();
                while (System.nanoTime() - time < 3000000000.0) {
                    //
                }
                telemetry.addData("BR Encoder: " , hardwareStore.backRight.getCurrentPosition());
                telemetry.update();

                 */

                mecanumDrivetrain.driveByGyro(15, MecanumDrivetrain.DIRECTION_FORWARD, AUTONOMOUS_SPEED, 0);
               //mecanumDrivetrain.driveByDistance(14, MecanumDrivetrain.DIRECTION_FORWARD, AUTONOMOUS_SPEED);
               //mecanumDrivetrain.forwardByTime(this,AUTONOMOUS_SPEED,0.5);

            }else {
                //hardwareStore.backRight.setPower(0);
            }
            if (gamepad1.b) {
                /*hardwareStore.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                hardwareStore.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardwareStore.frontLeft.setPower(AUTONOMOUS_SPEED);
                double time = System.nanoTime();
                while (System.nanoTime() - time < 3000000000.0) {
                    //
                }
                telemetry.addData("FL Encoder: " , hardwareStore.frontLeft.getCurrentPosition());
                telemetry.update();

                 */
                //mecanumDrivetrain.strafeRightByTime(this, AUTONOMOUS_SPEED, 0.5);
                mecanumDrivetrain.driveByGyro(15, MecanumDrivetrain.DIRECTION_STRAFE_RIGHT, AUTONOMOUS_SPEED, 0);
                //mecanumDrivetrain.driveByDistance(14, MecanumDrivetrain.DIRECTION_STRAFE_RIGHT, AUTONOMOUS_SPEED);
            }else {
                //hardwareStore.frontLeft.setPower(0);
            }

            if (gamepad1.x) {
                /*
                hardwareStore.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
                hardwareStore.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                hardwareStore.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardwareStore.frontRight.setPower(AUTONOMOUS_SPEED);
                double time = System.nanoTime();
                while (System.nanoTime() - time < 3000000000.0) {
                    //
                }
                telemetry.addData("FR Encoder: " , hardwareStore.frontRight.getCurrentPosition());
                telemetry.update();

                 */
                //mecanumDrivetrain.strafeLeftByTime(this, AUTONOMOUS_SPEED, 0.5);
                mecanumDrivetrain.driveByGyro(15, MecanumDrivetrain.DIRECTION_STRAFE_LEFT, AUTONOMOUS_SPEED, 0);
                //mecanumDrivetrain.driveByDistance(14, MecanumDrivetrain.DIRECTION_STRAFE_LEFT, AUTONOMOUS_SPEED);
            }else {
                //hardwareStore.frontRight.setPower(0);
            }
            if (gamepad2.dpad_left) {
                cargoGrabber.resetToLydiasFavoritePosition();
                //mecanumDrivetrain.gyroTurn(-90, AUTONOMOUS_SPEED, this);
            }
            if (gamepad1.dpad_right) {
               // mecanumDrivetrain.gyroTurn(90, AUTONOMOUS_SPEED, this);
            }

            if (gamepad1.dpad_up) {
                //mecanumDrivetrain.gyroTurn(180, AUTONOMOUS_SPEED, this);
            }
            if (gamepad1.dpad_down) {
                //mecanumDrivetrain.gyroTurn(360, AUTONOMOUS_SPEED, this);
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
