package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Diagnostic OpMode to test each motor individually.
 * Use this to verify:
 * 1. Motor names are correct in hardware config
 * 2. Motors are responding to commands
 * 3. Motor directions are correct
 * 
 * Controls:
 * - DPAD UP: Test Front Left
 * - DPAD DOWN: Test Back Left
 * - DPAD LEFT: Test Front Right
 * - DPAD RIGHT: Test Back Right
 * - A: Run all motors forward
 * - B: Run all motors backward
 * - X: Stop all motors
 */
@TeleOp(name = "Motor Diagnostic", group = "Testing")
public class
MotorDiagnostic extends LinearOpMode {
    
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    
    private static final double TEST_POWER = 0.3;
    
    @Override
    public void runOpMode() {
        // Initialize motors - ADJUST THESE NAMES TO MATCH YOUR CONFIG
        try {
            frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
            telemetry.addData("Front Left", "Found");
        } catch (Exception e) {
            telemetry.addData("Front Left", "NOT FOUND: " + e.getMessage());
        }
        
        try {
            frontRight = hardwareMap.get(DcMotor.class, "frontRight");
            telemetry.addData("Front Right", "Found");
        } catch (Exception e) {
            telemetry.addData("Front Right", "NOT FOUND: " + e.getMessage());
        }
        
        try {
            backLeft = hardwareMap.get(DcMotor.class, "backLeft");
            telemetry.addData("Back Left", "Found");
        } catch (Exception e) {
            telemetry.addData("Back Left", "NOT FOUND: " + e.getMessage());
        }
        
        try {
            backRight = hardwareMap.get(DcMotor.class, "backRight");
            telemetry.addData("Back Right", "Found");
        } catch (Exception e) {
            telemetry.addData("Back Right", "NOT FOUND: " + e.getMessage());
        }
        
        telemetry.addLine();
        telemetry.addLine("Press START when ready");
        telemetry.update();
        
        waitForStart();
        
        while (opModeIsActive()) {
            // Stop all motors first
            stopAllMotors();
            
            // Test individual motors with DPAD
            if (gamepad1.dpad_up && frontLeft != null) {
                frontLeft.setPower(TEST_POWER);
                telemetry.addData("Testing", "Front Left at " + TEST_POWER);
            }
            
            if (gamepad1.dpad_down && backLeft != null) {
                backLeft.setPower(TEST_POWER);
                telemetry.addData("Testing", "Back Left at " + TEST_POWER);
            }
            
            if (gamepad1.dpad_left && frontRight != null) {
                frontRight.setPower(TEST_POWER);
                telemetry.addData("Testing", "Front Right at " + TEST_POWER);
            }
            
            if (gamepad1.dpad_right && backRight != null) {
                backRight.setPower(TEST_POWER);
                telemetry.addData("Testing", "Back Right at " + TEST_POWER);
            }
            
            // Test all motors forward
            if (gamepad1.a) {
                setAllMotors(TEST_POWER);
                telemetry.addData("Testing", "All Forward at " + TEST_POWER);
            }
            
            // Test all motors backward
            if (gamepad1.b) {
                setAllMotors(-TEST_POWER);
                telemetry.addData("Testing", "All Backward at " + TEST_POWER);
            }
            
            // Stop
            if (gamepad1.x) {
                stopAllMotors();
                telemetry.addData("Status", "All Stopped");
            }
            
            // Display motor powers
            telemetry.addLine();
            telemetry.addLine("=== Current Motor Powers ===");
            if (frontLeft != null) {
                telemetry.addData("FL Power", "%.2f", frontLeft.getPower());
            }
            if (frontRight != null) {
                telemetry.addData("FR Power", "%.2f", frontRight.getPower());
            }
            if (backLeft != null) {
                telemetry.addData("BL Power", "%.2f", backLeft.getPower());
            }
            if (backRight != null) {
                telemetry.addData("BR Power", "%.2f", backRight.getPower());
            }
            
            telemetry.addLine();
            telemetry.addLine("=== Controls ===");
            telemetry.addLine("DPAD UP: Front Left");
            telemetry.addLine("DPAD DOWN: Back Left");
            telemetry.addLine("DPAD LEFT: Front Right");
            telemetry.addLine("DPAD RIGHT: Back Right");
            telemetry.addLine("A: All Forward");
            telemetry.addLine("B: All Backward");
            telemetry.addLine("X: Stop All");
            
            telemetry.update();
            sleep(50);
        }
    }
    
    private void stopAllMotors() {
        if (frontLeft != null) frontLeft.setPower(0);
        if (frontRight != null) frontRight.setPower(0);
        if (backLeft != null) backLeft.setPower(0);
        if (backRight != null) backRight.setPower(0);
    }
    
    private void setAllMotors(double power) {
        if (frontLeft != null) frontLeft.setPower(power);
        if (frontRight != null) frontRight.setPower(power);
        if (backLeft != null) backLeft.setPower(power);
        if (backRight != null) backRight.setPower(power);
    }
}
