package org.firstinspires.ftc.teamcode.commands.drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.constants.DriveConstants;
import org.firstinspires.ftc.teamcode.constants.OperatorConstants;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.util.InputProcessor;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * Teleop Command for Mecanum Drive
 * 
 * This command handles all driver input processing and sends commands
 * to the MecanumDriveSubsystem. It supports:
 * - Robot-centric and field-centric driving
 * - Precision mode (slow, controlled movement)
 * - Turbo mode (maximum speed)
 * - Input deadband and scaling
 * 
 * This is typically set as the default command for the drive subsystem.
 */
public class TeleopMecanumDrive extends CommandBase {
    
    // ==================== Dependencies ====================
    
    private final MecanumDriveSubsystem driveSubsystem;
    
    // Input suppliers for joystick values
    private final DoubleSupplier strafeSupplier;
    private final DoubleSupplier forwardSupplier;
    private final DoubleSupplier turnSupplier;
    
    // Input suppliers for mode buttons
    private final BooleanSupplier precisionModeSupplier;
    private final BooleanSupplier turboModeSupplier;
    
    // ==================== Constructor ====================
    
    /**
     * Creates a new TeleopMecanumDrive command.
     * 
     * @param driveSubsystem The mecanum drive subsystem
     * @param strafeSupplier Supplier for strafe input (left stick X)
     * @param forwardSupplier Supplier for forward input (left stick Y)
     * @param turnSupplier Supplier for turn input (right stick X)
     * @param precisionModeSupplier Supplier for precision mode button
     * @param turboModeSupplier Supplier for turbo mode button
     */
    public TeleopMecanumDrive(
            MecanumDriveSubsystem driveSubsystem,
            DoubleSupplier strafeSupplier,
            DoubleSupplier forwardSupplier,
            DoubleSupplier turnSupplier,
            BooleanSupplier precisionModeSupplier,
            BooleanSupplier turboModeSupplier) {
        
        this.driveSubsystem = driveSubsystem;
        this.strafeSupplier = strafeSupplier;
        this.forwardSupplier = forwardSupplier;
        this.turnSupplier = turnSupplier;
        this.precisionModeSupplier = precisionModeSupplier;
        this.turboModeSupplier = turboModeSupplier;
        
        // Declare subsystem dependencies
        addRequirements(driveSubsystem);
    }
    
    // ==================== Command Lifecycle ====================
    
    /**
     * Called once when the command is initially scheduled.
     */
    @Override
    public void initialize() {
        // Nothing to initialize - command runs continuously
    }
    
    /**
     * Called repeatedly while the command is scheduled.
     * This is where all the input processing and driving logic happens.
     */
    @Override
    public void execute() {
        // 1. Determine speed multiplier based on active mode
        double speedMultiplier = getSpeedMultiplier();
        
        // 2. Get raw inputs from suppliers
        double rawStrafe = strafeSupplier.getAsDouble();
        double rawForward = forwardSupplier.getAsDouble();
        double rawTurn = turnSupplier.getAsDouble();
        
        // 3. Process inputs (apply deadband and scale)
        double strafeSpeed = InputProcessor.processInput(
            rawStrafe,
            DriveConstants.CONTROLLER_DEADBAND,
            DriveConstants.MAX_DRIVE_SPEED * speedMultiplier
        );
        
        double forwardSpeed = InputProcessor.processInput(
            rawForward,
            DriveConstants.CONTROLLER_DEADBAND,
            DriveConstants.MAX_DRIVE_SPEED * speedMultiplier
        );
        
        double turnSpeed = InputProcessor.processInput(
            rawTurn,
            DriveConstants.CONTROLLER_DEADBAND,
            DriveConstants.MAX_ANGULAR_SPEED * speedMultiplier
        );
        
        // 4. Send processed values to subsystem
        driveSubsystem.drive(strafeSpeed, forwardSpeed, turnSpeed);
    }
    
    /**
     * Called once when the command ends or is interrupted.
     * 
     * @param interrupted true if the command was interrupted
     */
    @Override
    public void end(boolean interrupted) {
        // Stop the robot when command ends
        driveSubsystem.stop();
    }
    
    /**
     * Returns whether the command has finished.
     * This command never finishes on its own (it's a default command).
     * 
     * @return false - runs until interrupted
     */
    @Override
    public boolean isFinished() {
        return false;
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Determine the speed multiplier based on which mode is active.
     * 
     * Priority (if mutually exclusive):
     * 1. Precision mode (slowest)
     * 2. Turbo mode (fastest)
     * 3. Normal mode (default)
     * 
     * @return Speed multiplier (0.0 to 1.0)
     */
    private double getSpeedMultiplier() {
        boolean precisionActive = precisionModeSupplier.getAsBoolean();
        boolean turboActive = turboModeSupplier.getAsBoolean();
        
        // If modes are mutually exclusive
        if (OperatorConstants.MUTUALLY_EXCLUSIVE_MODES) {
            // Check priority
            if (OperatorConstants.PRECISION_PRIORITY) {
                // Precision has priority over turbo
                if (precisionActive) {
                    return DriveConstants.PRECISION_MULTIPLIER;
                } else if (turboActive) {
                    return DriveConstants.TURBO_MULTIPLIER;
                }
            } else {
                // Turbo has priority over precision
                if (turboActive) {
                    return DriveConstants.TURBO_MULTIPLIER;
                } else if (precisionActive) {
                    return DriveConstants.PRECISION_MULTIPLIER;
                }
            }
        } else {
            // If not mutually exclusive, precision takes priority anyway
            // (you typically want precision to override turbo for safety)
            if (precisionActive) {
                return DriveConstants.PRECISION_MULTIPLIER;
            } else if (turboActive) {
                return DriveConstants.TURBO_MULTIPLIER;
            }
        }
        
        // Default: normal speed
        return DriveConstants.NORMAL_MULTIPLIER;
    }
}
