package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.constants.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.util.InputProcessor;

/**
 * Advanced Diagnostic for Drive System
 * 
 * This tests the entire drive chain:
 * 1. Raw joystick inputs
 * 2. Processed inputs (after deadband)
 * 3. Subsystem commands
 * 4. Motor responses
 * 
 * Use this to identify where the signal is getting lost.
 */
@TeleOp(name = "Drive Diagnostic", group = "Testing")
public class DriveDiagnostic extends LinearOpMode {
    
    private MecanumDriveSubsystem driveSubsystem;
    
    @Override
    public void runOpMode() {
        // Initialize subsystem
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        
        try {
            driveSubsystem = new MecanumDriveSubsystem(hardwareMap);
            telemetry.addData("Subsystem", "Initialized");
        } catch (Exception e) {
            telemetry.addData("ERROR", e.getMessage());
            telemetry.addData("Stack", e.getStackTrace()[0].toString());
            telemetry.update();
            while (opModeIsActive()) {
                sleep(100);
            }
            return;
        }
        
        telemetry.addData("Status", "Ready");
        telemetry.update();
        
        waitForStart();
        
        while (opModeIsActive()) {
            // Get raw inputs
            double rawStrafe = -gamepad1.left_stick_x;
            double rawForward = -gamepad1.left_stick_y;
            double rawTurn = -gamepad1.right_stick_x;
            
            // Process inputs (same as TeleopMecanumDrive)
            double strafeSpeed = InputProcessor.processInput(
                rawStrafe,
                DriveConstants.CONTROLLER_DEADBAND,
                DriveConstants.MAX_DRIVE_SPEED
            );
            
            double forwardSpeed = InputProcessor.processInput(
                rawForward,
                DriveConstants.CONTROLLER_DEADBAND,
                DriveConstants.MAX_DRIVE_SPEED
            );
            
            double turnSpeed = InputProcessor.processInput(
                rawTurn,
                DriveConstants.CONTROLLER_DEADBAND,
                DriveConstants.MAX_ANGULAR_SPEED
            );
            
            // Send to subsystem
            driveSubsystem.drive(strafeSpeed, forwardSpeed, turnSpeed);
            
            // Get motor powers for verification
            double[] motorPowers = driveSubsystem.getMotorVelocities();
            
            // TELEMETRY
            telemetry.clearAll();
            telemetry.addLine("=== RAW INPUTS ===");
            telemetry.addData("Raw Strafe (LX)", "%.3f", rawStrafe);
            telemetry.addData("Raw Forward (LY)", "%.3f", rawForward);
            telemetry.addData("Raw Turn (RX)", "%.3f", rawTurn);
            
            telemetry.addLine();
            telemetry.addLine("=== PROCESSED ===");
            telemetry.addData("Strafe Speed", "%.3f", strafeSpeed);
            telemetry.addData("Forward Speed", "%.3f", forwardSpeed);
            telemetry.addData("Turn Speed", "%.3f", turnSpeed);
            
            telemetry.addLine();
            telemetry.addLine("=== MOTOR VELOCITIES ===");
            telemetry.addData("FL Velocity", "%.1f", motorPowers[0]);
            telemetry.addData("FR Velocity", "%.1f", motorPowers[1]);
            telemetry.addData("BL Velocity", "%.1f", motorPowers[2]);
            telemetry.addData("BR Velocity", "%.1f", motorPowers[3]);
            
            telemetry.addLine();
            telemetry.addLine("=== ENCODER POSITIONS ===");
            double[] encoders = driveSubsystem.getEncoderPositions();
            telemetry.addData("FL Encoder", "%.0f", encoders[0]);
            telemetry.addData("FR Encoder", "%.0f", encoders[1]);
            telemetry.addData("BL Encoder", "%.0f", encoders[2]);
            telemetry.addData("BR Encoder", "%.0f", encoders[3]);
            
            telemetry.addLine();
            telemetry.addLine("=== STATUS ===");
            telemetry.addData("Drive Mode", 
                driveSubsystem.isFieldCentric() ? "Field-Centric" : "Robot-Centric");
            telemetry.addData("Deadband", DriveConstants.CONTROLLER_DEADBAND);
            telemetry.addData("Max Speed", DriveConstants.MAX_DRIVE_SPEED);
            
            telemetry.addLine();
            telemetry.addData("Instructions", "Move sticks and watch values");
            
            telemetry.update();
            
            sleep(20); // Small delay for readability
        }
    }
}
