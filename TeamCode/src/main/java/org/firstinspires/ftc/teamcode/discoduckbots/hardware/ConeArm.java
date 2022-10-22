package org.firstinspires.ftc.teamcode.discoduckbots.hardware;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class ConeArm {

    private DcMotor coneLift;
    private Servo coneGrabber;
    private boolean resetInProgress = false;

    public ConeArm(DcMotor wobbleMoverMotor, Servo wobbleGrabber) {
       this.coneLift = wobbleMoverMotor;
        this.coneGrabber = wobbleGrabber;
        coneLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        coneLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        coneLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    public void drop(LinearOpMode opmode) {
        coneLift.setPower(-0.5);
        opmode.sleep(2000);
        coneLift.setPower(0);
        release();
    }

    public void print() {
        Log.d("ftc3", "cargoMotor " + coneLift.getCurrentPosition());
    }

    public void dropByEncoder(int revolutions){
        coneLift.setDirection(DcMotorSimple.Direction.REVERSE);
        coneLift.setTargetPosition(coneLift.getCurrentPosition() + revolutions);
        coneLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        while (coneLift.getTargetPosition() > coneLift.getCurrentPosition()){
            coneLift.setPower(0.5);
        }
        coneLift.setPower(0.0);
        release();
    }

    public void resetToLydiasFavoritePosition() {
        resetToLydiasFavoritePosition(540);
    }
    public void resetArmTeleop() {
        resetToLydiasFavoritePosition(0);
    }

    public void resetGrabberAsync() { resetArmAsync(540);}
    public void resetGrabberAsyncTeleop() {
        resetArmAsync(0);
    }

    public void resetArmAsync(int armPosition) {
        if(!resetInProgress) {
            resetInProgress = true;
            grab();

            //cargoMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            coneLift.setTargetPosition(armPosition);
            coneLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //cargoMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            coneLift.setPower(-0.75);
            Log.d("ftc-reset", "exiting resetArm");
        } else {
            Log.d("ftc-reset", "ignoring reset as in progress");
        }
    }

    public void liftByEncoderAsync(int revolutions) {
        //if (!resetInProgress) {
         //   resetInProgress = true;

            coneLift.setDirection(DcMotorSimple.Direction.FORWARD);
            coneLift.setTargetPosition(coneLift.getCurrentPosition() + revolutions);
            coneLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //cargoMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            coneLift.setPower(0.5);
        //}
    }


    public void stopIfNotBusy() {
        Log.d("ftc-reset", "curr pos: " +  coneLift.getCurrentPosition());
        if ( resetInProgress ) {
            if (coneLift.getCurrentPosition() <= 0) {
                Log.d("ftc-reset", "cargoMotor stopping async ");
                coneLift.setPower(0.0);
                coneLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                resetInProgress = false;
            } else {
                Log.d("ftc-reset", "cargoMotor continuing power ");
                //cargoMotor.setPower(-0.75);
            }
        } else {
            coneLift.setPower(0);
        }

    }
    public void resetToLydiasFavoritePosition(int position){
        grab();

        //cargoMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        coneLift.setTargetPosition(position);
        coneLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       // cargoMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        coneLift.setPower(-0.75);


        while(coneLift.isBusy()) {
            Log.d("ftc", "cargoMotor in loop " + coneLift.getCurrentPosition());
        }
        Log.d("ftc", "cargoMotor out of loop ");
        coneLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        coneLift.setPower(0.0);

    }

    public void lowerByEncoder(int revolutions){

        coneLift.setDirection(DcMotorSimple.Direction.REVERSE);
        //cargoMotor.setTargetPosition(cargoMotor.getCurrentPosition() + revolutions);
        //cargoMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        coneLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //cargoMotor.setPower(0.5);
        while (coneLift.getTargetPosition() > coneLift.getCurrentPosition()){
            coneLift.setPower(0.5);
            Log.d("FTC-Arm", "c: " +
                    coneLift.getCurrentPosition() + " t " + coneLift.getTargetPosition());
        }
        //cargoMotor.setPower(0.5);
       /* while(cargoMotor.isBusy()) {

        }*/
        coneLift.setPower(0.0);

    }


    public void grabAndLiftByEncoder(int revolutions, LinearOpMode opMode){
        grab();
        opMode.sleep(500);
        coneLift.setDirection(DcMotorSimple.Direction.FORWARD);
        coneLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        coneLift.setTargetPosition(coneLift.getCurrentPosition() + revolutions);

        while (coneLift.getTargetPosition() > coneLift.getCurrentPosition()){
            coneLift.setPower(0.5);
        }

        coneLift.setPower(0.0);

    }
/*
    public void liftByEncoder(int revolutions){

        cargoMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        cargoMotor.setTargetPosition(cargoMotor.getCurrentPosition() + revolutions);
        //cargoMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        cargoMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //cargoMotor.setPower(0.5);
        while (cargoMotor.getTargetPosition() > cargoMotor.getCurrentPosition()){
            cargoMotor.setPower(0.5);
            Log.d("FTC-Arm", "c: " +
                    cargoMotor.getCurrentPosition() + " t " + cargoMotor.getTargetPosition());
        }

        cargoMotor.setPower(0.0);

    }
*/
    public void liftByEncoder(int revolutions){

        coneLift.setDirection(DcMotorSimple.Direction.FORWARD);
        coneLift.setTargetPosition(coneLift.getCurrentPosition() + revolutions);
        coneLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //cargoMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        coneLift.setPower(0.5);


       while(coneLift.isBusy()) {

        }
        //cargoMotor.setPower(0.0);

    }

    public void liftInch(LinearOpMode opmode) {
        coneLift.setPower(0.5);
        opmode.sleep(325);
        coneLift.setPower(0);
    }
    public void dropLift(LinearOpMode opmode) {
        coneLift.setPower(-0.5);
        opmode.sleep(2000);
        coneLift.setPower(0);
        release();
        coneLift.setPower(0.5);
        opmode.sleep(1000);
        coneLift.setPower(0);
    }
    public void resetPositionAs0 () {
        coneLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void lower(double speed) {
        coneLift.setPower(-1 * speed);
    }

    public void lift(double speed) { coneLift.setPower(speed);
    }

    public void stop() {
        coneLift.setPower(0);
    }

    public void grab() {
        coneGrabber.setDirection(Servo.Direction.REVERSE);
        coneGrabber.setPosition(0);
    }

    public void release() {
        coneGrabber.setDirection(Servo.Direction.REVERSE);
        coneGrabber.setPosition(0.50);

    }

    public void open() {
        coneGrabber.setDirection(Servo.Direction.REVERSE);
        coneGrabber.setPosition(1);
    }

    public double printServoValue(){
        return coneGrabber.getPosition();
    }
}
