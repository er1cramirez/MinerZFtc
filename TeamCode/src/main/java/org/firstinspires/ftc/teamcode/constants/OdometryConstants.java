package org.firstinspires.ftc.teamcode.constants;

/**
 * Constants for the odometry system.
 * This configuration is for the goBILDA Pinpoint Odometry Computer.
 */
public final class OdometryConstants {
    
    // ==================== Odometry Hardware ====================
    
    /**
     * Using goBILDA Pinpoint Odometry Computer
     */
    public static final boolean USE_PINPOINT = true;
    
    /**
     * Pinpoint device name in Robot Controller configuration
     */
    public static final String PINPOINT_NAME = "pinpoint";
    
    /**
     * IMU device name in Robot Controller configuration
     */
    public static final String IMU_NAME = "imu";
    
    // ==================== Pinpoint Configuration ====================
    
    /**
     * Offset of X pod from robot center (mm)
     * Positive = forward from center
     * Measure from robot center to the X encoder pod
     */
    public static final double X_OFFSET_MM = 0.0;
    
    /**
     * Offset of Y pod from robot center (mm)
     * Positive = left from center
     * Measure from robot center to the Y encoder pod
     */
    public static final double Y_OFFSET_MM = 0.0;
    
    /**
     * Pod resolution in ticks per mm
     * goBILDA Pinpoint default: check your specific pods
     * Common values: 13.26291192 for standard goBILDA pods
     */
    public static final double TICKS_PER_MM = 13.26291192;
    
    // ==================== Pose Estimation ====================
    
    /**
     * Use IMU for heading in odometry fusion
     * Pinpoint can use its own gyro or the Control Hub IMU
     */
    public static final boolean USE_IMU_FOR_HEADING = true;
    
    /**
     * Initial pose X coordinate (inches)
     */
    public static final double INITIAL_POSE_X = 0.0;
    
    /**
     * Initial pose Y coordinate (inches)
     */
    public static final double INITIAL_POSE_Y = 0.0;
    
    /**
     * Initial heading (degrees)
     */
    public static final double INITIAL_HEADING = 0.0;
    
    // ==================== Units Conversion ====================
    
    /**
     * Millimeters to inches conversion
     */
    public static final double MM_TO_INCHES = 0.0393701;
    
    /**
     * Inches to millimeters conversion
     */
    public static final double INCHES_TO_MM = 25.4;
    
    // ==================== Backup Configuration ====================
    
    /**
     * Fallback to motor encoders if Pinpoint fails
     */
    public static final boolean ENABLE_MOTOR_ENCODER_FALLBACK = false;
    
    /**
     * Motor encoder ticks per revolution (for GoBILDA motors if using fallback)
     * Yellow Jacket 312 RPM: 537.7
     * Yellow Jacket 435 RPM: 383.6
     * Yellow Jacket 1150 RPM: 145.1
     */
    public static final double MOTOR_TICKS_PER_REV = 537.7;
    
    /**
     * Calculate ticks to inches for motor encoders (backup)
     */
    public static final double MOTOR_TICKS_TO_INCHES = 
        (DriveConstants.WHEEL_DIAMETER * Math.PI) / MOTOR_TICKS_PER_REV;
    
    // Prevent instantiation
    private OdometryConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
