
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum Drive Example", group = "Examples")
public class HelloMotor extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void runOpMode() {

        // Initialize motors using their configured hardware names
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Reverse the right-side motors for a typical drivetrain setup
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Use open-loop power control
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            double frontLeftPower  = y + x + turn;
            double backLeftPower   = y - x + turn;
            double frontRightPower = y - x - turn;
            double backRightPower  = y + x - turn;

            // Normalize powers so none exceed [-1, 1]
            double max = Math.max(1.0, Math.max(
                    Math.max(Math.abs(frontLeftPower),
                            Math.abs(backLeftPower)),
                    Math.max(Math.abs(frontRightPower),
                            Math.abs(backRightPower))
            ));

            frontLeft.setPower(frontLeftPower / max);
            backLeft.setPower(backLeftPower / max);
            frontRight.setPower(frontRightPower / max);
            backRight.setPower(backRightPower / max);

            telemetry.addData("Forward", "%.2f", y);
            telemetry.addData("Strafe", "%.2f", x);
            telemetry.addData("Turn", "%.2f", turn);
            telemetry.update();
        }
    }
}
