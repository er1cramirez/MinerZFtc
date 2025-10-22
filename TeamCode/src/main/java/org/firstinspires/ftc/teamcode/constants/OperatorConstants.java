package org.firstinspires.ftc.teamcode.constants;

import com.arcrobotics.ftclib.gamepad.GamepadKeys;

/**
 * Constants for operator interface controls.
 * Defines button mappings and gamepad configurations.
 */
public final class OperatorConstants {
    
    // ==================== Gamepad Assignments ====================
    
    /**
     * Driver gamepad port (gamepad1)
     */
    public static final int DRIVER_PORT = 0;
    
    /**
     * Operator/tool gamepad port (gamepad2)
     */
    public static final int OPERATOR_PORT = 1;
    
    // ==================== Driver Controls - Drive ====================
    
    /**
     * Left stick Y axis controls forward/backward movement
     * Note: Y axis is typically inverted in FTC
     */
    // Handled directly via gamepad.getLeftY()
    
    /**
     * Left stick X axis controls strafe (left/right) movement
     */
    // Handled directly via gamepad.getLeftX()
    
    /**
     * Right stick X axis controls rotation (turning)
     */
    // Handled directly via gamepad.getRightX()
    
    // ==================== Driver Controls - Drive Modes ====================
    
    /**
     * Button to activate precision mode (slow, controlled movement)
     * Hold this button for precise maneuvering
     */
    public static final GamepadKeys.Button PRECISION_MODE_BUTTON = 
        GamepadKeys.Button.LEFT_BUMPER;
    
    /**
     * Button to activate turbo mode (maximum speed)
     * Hold this button for fast movement
     */
    public static final GamepadKeys.Button TURBO_MODE_BUTTON = 
        GamepadKeys.Button.RIGHT_BUMPER;
    
    /**
     * Button to toggle field-centric driving mode
     * Press once to enable, press again to disable
     */
    public static final GamepadKeys.Button TOGGLE_FIELD_CENTRIC_BUTTON = 
        GamepadKeys.Button.START;
    
    /**
     * Button to reset robot heading/orientation
     * Useful if the robot becomes disoriented during field-centric driving
     */
    public static final GamepadKeys.Button RESET_HEADING_BUTTON = 
        GamepadKeys.Button.BACK;
    
    // ==================== Driver Controls - Utility ====================
    
    /**
     * Button to reset odometry to zero position
     * Use at the start of a match or when repositioning
     */
    public static final GamepadKeys.Button RESET_ODOMETRY_BUTTON = 
        GamepadKeys.Button.DPAD_DOWN;
    
    // ==================== Operator Controls ====================
    // Add operator/tool gamepad controls here as you expand the robot
    // Examples:
    // public static final GamepadKeys.Button INTAKE_IN = GamepadKeys.Button.A;
    // public static final GamepadKeys.Button INTAKE_OUT = GamepadKeys.Button.B;
    
    // ==================== Control Behavior ====================
    
    /**
     * Whether precision and turbo modes are mutually exclusive
     * If true, only one can be active at a time
     */
    public static final boolean MUTUALLY_EXCLUSIVE_MODES = true;
    
    /**
     * Priority if both buttons pressed (when mutually exclusive)
     * true = precision has priority, false = turbo has priority
     */
    public static final boolean PRECISION_PRIORITY = true;
    
    // Prevent instantiation
    private OperatorConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
