package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.drive.TeleopMecanumDriveSimple;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

/**
 * MainTeleOp using simplified command
 * This helps identify if the issue is with the full TeleopMecanumDrive command
 */
@TeleOp(name = "Main TeleOp SIMPLE", group = "Testing")
public class MainTeleOpSimple extends CommandOpMode {
    
    private MecanumDriveSubsystem driveSubsystem;
    private GamepadEx driverGamepad;
    private TeleopMecanumDriveSimple driveCommand;
    
    @Override
    public void initialize() {
        System.out.println("===== MainTeleOpSimple INITIALIZE START =====");
        
        // Initialize subsystem
        driveSubsystem = new MecanumDriveSubsystem(hardwareMap);
        System.out.println("Subsystem created");
        
        // Initialize gamepad
        driverGamepad = new GamepadEx(gamepad1);
        System.out.println("GamepadEx created");
        
        // Create simple command
        driveCommand = new TeleopMecanumDriveSimple(
            driveSubsystem,
            () -> -driverGamepad.getLeftX(),
            () -> -driverGamepad.getLeftY(),
            () -> -driverGamepad.getRightX()
        );
        System.out.println("Command created");
        
        // Register subsystem
        register(driveSubsystem);
        System.out.println("Subsystem registered");
        
        // Set default command
        driveSubsystem.setDefaultCommand(driveCommand);
        System.out.println("Default command set");
        
        System.out.println("===== MainTeleOpSimple INITIALIZE COMPLETE =====");
        
        telemetry.addLine("Initialization Complete");
        telemetry.addLine("Check Logcat for debug messages");
        telemetry.update();
    }
    
    @Override
    public void run() {
        // CRITICAL: Read gamepad
        driverGamepad.readButtons();
        
        // Run scheduler
        super.run();
        
        // Simple telemetry
        telemetry.clearAll();
        telemetry.addData("Left X", "%.2f", driverGamepad.getLeftX());
        telemetry.addData("Left Y", "%.2f", driverGamepad.getLeftY());
        telemetry.addData("Right X", "%.2f", driverGamepad.getRightX());
        telemetry.addData("Command Scheduled", driveCommand.isScheduled());
        telemetry.update();
    }
}
