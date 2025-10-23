package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.drive.TeleopMecanumDrive;
import org.firstinspires.ftc.teamcode.constants.OperatorConstants;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.commands.drive.FieldCentricToggle;

/**
 * Main TeleOp OpMode
 * 
 * This is the primary teleop mode for the robot. It initializes all subsystems,
 * creates commands, binds controls, and manages the command scheduler.
 * 
 * Features:
 * - Mecanum drive with robot-centric and field-centric modes
 * - Precision and turbo drive modes
 * - Field-centric toggle
 * - Heading reset
 * - Comprehensive telemetry
 */
@TeleOp(name = "Main TeleOp", group = "Competition")
public class MainTeleOp extends CommandOpMode {
    
    // ==================== Subsystems ====================
    
    private MecanumDriveSubsystem driveSubsystem;
    
    // ==================== Controllers ====================
    
    private GamepadEx driverGamepad;
    // private GamepadEx operatorGamepad; // Add when you need operator controls
    
    // ==================== Commands ====================
    
    private TeleopMecanumDrive driveCommand;
    
    // ==================== Helpers ====================
    
    private FieldCentricToggle fieldCentricToggle;
    
    // ==================== Initialization ====================
    
    /**
     * Initialize the OpMode.
     * This runs once when the OpMode is selected.
     */
    @Override
    public void initialize() {
        // ===== 1. Initialize Subsystems =====
        telemetry.addData("Status", "Initializing subsystems...");
        telemetry.update();
        
        driveSubsystem = new MecanumDriveSubsystem(hardwareMap);
        
        // ===== 2. Initialize Gamepads =====
        telemetry.addData("Status", "Initializing gamepads...");
        telemetry.update();
        
        driverGamepad = new GamepadEx(gamepad1);
        // operatorGamepad = new GamepadEx(gamepad2); // Add when needed
        
        // ===== 3. Create Commands =====
        telemetry.addData("Status", "Creating commands...");
        telemetry.update();
        
        driveCommand = new TeleopMecanumDrive(
            driveSubsystem,
            () -> -driverGamepad.getLeftX(),   // Strafe (negative for correct direction)
            () -> -driverGamepad.getLeftY(),   // Forward (negative because Y is inverted)
            () -> -driverGamepad.getRightX(),  // Turn (negative for correct rotation)
            () -> driverGamepad.getButton(OperatorConstants.PRECISION_MODE_BUTTON),
            () -> driverGamepad.getButton(OperatorConstants.TURBO_MODE_BUTTON)
        );
        
        // ===== 4. Set Default Commands =====
        telemetry.addData("Status", "Setting default commands...");
        telemetry.update();
        
        driveSubsystem.setDefaultCommand(driveCommand);
        
        // ===== 5. Initialize Helpers =====
        fieldCentricToggle = new FieldCentricToggle(false); // Start in robot-centric
        
        // ===== 6. Configure Button Bindings =====
        telemetry.addData("Status", "Configuring controls...");
        telemetry.update();
        
        configureButtonBindings();
        
        // ===== 7. Register Subsystems =====
        register(driveSubsystem);
        
        // ===== 8. Initialization Complete =====
        telemetry.clearAll();
        telemetry.addLine("=================================");
        telemetry.addLine("    INITIALIZATION COMPLETE");
        telemetry.addLine("=================================");
        telemetry.addLine();
        telemetry.addData("Drive Mode", "Robot-Centric (default)");
        telemetry.addLine();
        telemetry.addLine("Controls:");
        telemetry.addLine("  Left Stick = Move");
        telemetry.addLine("  Right Stick X = Turn");
        telemetry.addLine("  Left Bumper = Precision");
        telemetry.addLine("  Right Bumper = Turbo");
        telemetry.addLine("  Start = Toggle Field-Centric");
        telemetry.addLine("  Back = Reset Heading");
        telemetry.addLine();
        telemetry.addData("Status", "Ready to Start");
        telemetry.update();
    }
    
    // ==================== Button Configuration ====================
    
    /**
     * Configure all button bindings for the driver gamepad.
     */
    private void configureButtonBindings() {
        
        // ===== Field-Centric Toggle =====
        // Press START to toggle between robot-centric and field-centric
        driverGamepad.getGamepadButton(OperatorConstants.TOGGLE_FIELD_CENTRIC_BUTTON)
            .whenPressed(() -> {
                // Update toggle state
                boolean newState = fieldCentricToggle.update(true);
                driveSubsystem.setFieldCentric(newState);
                
                // Provide feedback
                if (newState) {
                    gamepad1.rumble(200); // Short rumble for field-centric ON
                }
            })
            .whenReleased(() -> {
                // Update toggle helper to track button release
                fieldCentricToggle.update(false);
            });
        
        // ===== Reset Heading =====
        // Press BACK to reset the IMU heading to zero
        driverGamepad.getGamepadButton(OperatorConstants.RESET_HEADING_BUTTON)
            .whenPressed(() -> {
                driveSubsystem.resetHeading();
                gamepad1.rumble(100); // Short rumble for confirmation
            });
        
        // ===== Reset Odometry (Optional) =====
        // Press DPAD_DOWN to reset odometry to (0, 0, 0)
        driverGamepad.getGamepadButton(OperatorConstants.RESET_ODOMETRY_BUTTON)
            .whenPressed(() -> {
                driveSubsystem.resetOdometry();
                gamepad1.rumble(100); // Short rumble for confirmation
            });
    }
    
    // ==================== Main Loop ====================
    
    /**
     * This runs continuously after initialize() and during the match.
     * The CommandScheduler handles running commands, but we can add
     * custom telemetry or other periodic tasks here.
     */
    @Override
    public void run() {
        driverGamepad.readButtons();
        // Run the command scheduler (handles all commands and subsystems)
        super.run();
        
        // Update telemetry
        updateTelemetry();
    }
    
    // ==================== Telemetry ====================
    
    /**
     * Update telemetry with current robot state.
     * This provides real-time feedback to the driver station.
     */
    private void updateTelemetry() {
        // Clear previous data
        telemetry.clearAll();
        
        // ===== Drive Status =====
        telemetry.addLine("=== DRIVE STATUS ===");
        telemetry.addData("Mode", 
            driveSubsystem.isFieldCentric() ? "Field-Centric" : "Robot-Centric");
        telemetry.addData("Heading", "%.1f°", driveSubsystem.getHeading());
        
        // ===== Position =====
        telemetry.addLine();
        telemetry.addLine("=== POSITION ===");
        telemetry.addData("X", "%.2f in", driveSubsystem.getPose().getX());
        telemetry.addData("Y", "%.2f in", driveSubsystem.getPose().getY());
        telemetry.addData("Rotation", "%.1f°", 
            Math.toDegrees(driveSubsystem.getPose().getRotation().getRadians()));
        
        // ===== Drive Mode =====
        telemetry.addLine();
        telemetry.addLine("=== DRIVE MODE ===");
        boolean precisionActive = gamepad1.left_bumper;
        boolean turboActive = gamepad1.right_bumper;
        String currentMode = precisionActive ? "PRECISION (30%)" : 
                            turboActive ? "TURBO (100%)" : 
                            "NORMAL (100%)";
        telemetry.addData("Speed", currentMode);
        
        // ===== Motor Info (Optional - comment out if too cluttered) =====
        /*
        telemetry.addLine();
        telemetry.addLine("=== MOTORS ===");
        double[] encoders = driveSubsystem.getEncoderPositions();
        telemetry.addData("FL/FR", "%.0f / %.0f", encoders[0], encoders[1]);
        telemetry.addData("BL/BR", "%.0f / %.0f", encoders[2], encoders[3]);
        */
        
        // ===== Controls Reminder =====
        telemetry.addLine();
        telemetry.addLine("=== CONTROLS ===");
        telemetry.addLine("START: Toggle Field-Centric");
        telemetry.addLine("BACK: Reset Heading");

//        Add joystick info if desired
        telemetry.addData("Left Stick", "X: %.2f, Y: %.2f",
            -driverGamepad.getLeftX(), -driverGamepad.getLeftY());
        telemetry.addData("Right Stick X", "%.2f", -driverGamepad.getRightX());
        
        // Update the display
        telemetry.update();
    }
}
