
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum Drive Example", group = "Examples")
public class HelloMotor extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private float speed = 0.5f;

    @Override
    public void runOpMode() {

        // Initialize motors using their configured hardware names
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");//2
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");//1
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");//3
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");//0

        // Reverse the right-side motors for a typical drivetrain setup
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        // Use open-loop power control
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Drivetrain initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Gamepad left stick: translation
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;

            // Gamepad right stick: rotation
            double turn = gamepad1.right_stick_x;

            // Mecanum wheel power calculations
            double leftFrontPower  = y + x + turn;
            double leftBackPower   = y - x + turn;
            double rightFrontPower = y - x - turn;
            double rightBackPower  = y + x - turn;

            // Normalize powers so none exceed [-1, 1]
            double max = Math.max(1.0, Math.max(
                    Math.max(Math.abs(leftFrontPower),
                            Math.abs(leftBackPower)),
                    Math.max(Math.abs(rightFrontPower),
                            Math.abs(rightBackPower))
            ));

            leftFront.setPower(leftFrontPower / max * speed);
            leftBack.setPower(leftBackPower / max * speed);
            rightFront.setPower(rightFrontPower / max * speed);
            rightBack.setPower(rightBackPower / max * speed);

            telemetry.addData("Forward", "%.2f", y);
            telemetry.addData("Strafe", "%.2f", x);
            telemetry.addData("Turn", "%.2f", turn);
            telemetry.update();
        }
    }
}
