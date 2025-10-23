package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.drive.TeleopMecanumDrive;
import org.firstinspires.ftc.teamcode.constants.OperatorConstants;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

/**
 * Simplified MainTeleOp for debugging
 * This version has extensive logging to identify the issue
 */
@TeleOp(name = "Main TeleOp DEBUG", group = "Testing")
public class MainTeleOpDebug extends CommandOpMode {
    
    private MecanumDriveSubsystem driveSubsystem;
    private GamepadEx driverGamepad;
    private TeleopMecanumDrive driveCommand;
    
    private int loopCount = 0;
    
    @Override
    public void initialize() {
        telemetry.addLine("=== INITIALIZATION START ===");
        telemetry.update();
        
        // 1. Initialize Subsystem
        try {
            driveSubsystem = new MecanumDriveSubsystem(hardwareMap);
            telemetry.addData("Subsystem", "✓ OK");
        } catch (Exception e) {
            telemetry.addData("Subsystem", "✗ FAILED: " + e.getMessage());
            telemetry.update();
            return;
        }
        
        // 2. Initialize Gamepad
        try {
            driverGamepad = new GamepadEx(gamepad1);
            telemetry.addData("Gamepad", "✓ OK");
        } catch (Exception e) {
            telemetry.addData("Gamepad", "✗ FAILED: " + e.getMessage());
            telemetry.update();
            return;
        }
        
        // 3. Create Command
        try {
            driveCommand = new TeleopMecanumDrive(
                driveSubsystem,
                () -> -driverGamepad.getLeftX(),
                () -> -driverGamepad.getLeftY(),
                () -> -driverGamepad.getRightX(),
                () -> driverGamepad.getButton(OperatorConstants.PRECISION_MODE_BUTTON),
                () -> driverGamepad.getButton(OperatorConstants.TURBO_MODE_BUTTON)
            );
            telemetry.addData("Command", "✓ OK");
        } catch (Exception e) {
            telemetry.addData("Command", "✗ FAILED: " + e.getMessage());
            telemetry.update();
            return;
        }
        
        // 4. Register Subsystem
        try {
            register(driveSubsystem);
            telemetry.addData("Register", "✓ OK");
        } catch (Exception e) {
            telemetry.addData("Register", "✗ FAILED: " + e.getMessage());
            telemetry.update();
            return;
        }
        
        // 5. Set Default Command
        try {
            driveSubsystem.setDefaultCommand(driveCommand);
            telemetry.addData("Default Cmd", "✓ OK");
        } catch (Exception e) {
            telemetry.addData("Default Cmd", "✗ FAILED: " + e.getMessage());
            telemetry.update();
            return;
        }
        
        telemetry.addLine();
        telemetry.addLine("=== INITIALIZATION COMPLETE ===");
        telemetry.addLine("Press START to begin");
        telemetry.update();
    }
    
    @Override
    public void run() {
        // CRITICAL: Update gamepad BEFORE scheduler
        driverGamepad.readButtons();
        
        // Run the scheduler (this executes commands)
        super.run();
        
        // Count loops
        loopCount++;
        
        // Detailed telemetry
        telemetry.clearAll();
        telemetry.addLine("=== DEBUG TELEMETRY ===");
        telemetry.addData("Loop Count", loopCount);
        telemetry.addLine();
        
        // Raw gamepad values
        telemetry.addLine("--- RAW GAMEPAD ---");
        telemetry.addData("Left X", "%.3f", gamepad1.left_stick_x);
        telemetry.addData("Left Y", "%.3f", gamepad1.left_stick_y);
        telemetry.addData("Right X", "%.3f", gamepad1.right_stick_x);
        telemetry.addData("L Bumper", gamepad1.left_bumper);
        telemetry.addData("R Bumper", gamepad1.right_bumper);
        telemetry.addLine();
        
        // GamepadEx values
        telemetry.addLine("--- GAMEPAD EX ---");
        telemetry.addData("Ex Left X", "%.3f", driverGamepad.getLeftX());
        telemetry.addData("Ex Left Y", "%.3f", driverGamepad.getLeftY());
        telemetry.addData("Ex Right X", "%.3f", driverGamepad.getRightX());
        telemetry.addLine();
        
        // Subsystem state
        telemetry.addLine("--- SUBSYSTEM ---");
        telemetry.addData("Field-Centric", driveSubsystem.isFieldCentric());
        double[] encoders = driveSubsystem.getEncoderPositions();
        telemetry.addData("FL Encoder", "%.0f", encoders[0]);
        telemetry.addData("FR Encoder", "%.0f", encoders[1]);
        telemetry.addLine();
        
        // Command status
        telemetry.addLine("--- COMMAND STATUS ---");
        telemetry.addData("Default Cmd", driveCommand != null ? "Set" : "NULL");
        telemetry.addData("Is Scheduled", driveCommand != null && driveCommand.isScheduled());
        
        telemetry.update();
    }
}
