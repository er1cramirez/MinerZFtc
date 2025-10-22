package org.firstinspires.ftc.teamcode.constants;

/**
 * Constants for the Mecanum Drive subsystem.
 * Adjust these values based on your robot's configuration and testing.
 */
public final class DriveConstants {
    
    // ==================== Hardware Configuration ====================
    
    /**
     * Motor names as configured in the Robot Controller
     */
    public static final String FRONT_LEFT_MOTOR = "frontLeft";
    public static final String FRONT_RIGHT_MOTOR = "frontRight";
    public static final String BACK_LEFT_MOTOR = "backLeft";
    public static final String BACK_RIGHT_MOTOR = "backRight";
    
    /**
     * Motor inversions - adjust during testing to ensure correct movement
     * True = motor runs in reverse
     */
    public static final boolean INVERT_FRONT_LEFT = false;
    public static final boolean INVERT_FRONT_RIGHT = true;
    public static final boolean INVERT_BACK_LEFT = false;
    public static final boolean INVERT_BACK_RIGHT = true;
    
    // ==================== Physical Properties ====================
    
    /**
     * Distance between left and right wheels (inches)
     */
    public static final double TRACK_WIDTH = 13.5;
    
    /**
     * Distance between front and back wheels (inches)
     */
    public static final double WHEEL_BASE = 13.5;
    
    /**
     * Wheel diameter for GoBILDA 96mm Mecanum wheels (inches)
     */
    public static final double WHEEL_DIAMETER = 3.78;
    
    // ==================== Speed Limits ====================
    
    /**
     * Maximum drive speed (motor power scale, 0.0 to 1.0)
     * Reduce this if robot is too fast or hard to control
     */
    public static final double MAX_DRIVE_SPEED = 1.0;
    
    /**
     * Maximum angular/turn speed (motor power scale, 0.0 to 1.0)
     * Reduce this if turning is too aggressive
     */
    public static final double MAX_ANGULAR_SPEED = 1.0;
    
    // ==================== Speed Multipliers ====================
    
    /**
     * Speed multiplier for precision mode (slow and controlled)
     */
    public static final double PRECISION_MULTIPLIER = 0.3;
    
    /**
     * Speed multiplier for turbo mode (maximum speed)
     */
    public static final double TURBO_MULTIPLIER = 1.0;
    
    /**
     * Speed multiplier for normal mode (balanced)
     */
    public static final double NORMAL_MULTIPLIER = 1.0;
    
    // ==================== Input Processing ====================
    
    /**
     * Joystick deadband to eliminate drift and small unintended movements
     * Values below this threshold are treated as zero
     */
    public static final double CONTROLLER_DEADBAND = 0.1;
    
    // ==================== Drive Behavior ====================
    
    /**
     * Default field-centric mode state on initialization
     */
    public static final boolean DEFAULT_FIELD_CENTRIC = false;
    
    /**
     * Whether to square inputs for finer control at low speeds
     * True = more precise control at low speeds, less at high speeds
     */
    public static final boolean SQUARE_INPUTS = false;
    
    // Prevent instantiation
    private DriveConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
