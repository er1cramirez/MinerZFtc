package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.DriveConstants;
import org.firstinspires.ftc.teamcode.constants.OdometryConstants;

// Import for Pinpoint - adjust based on your SDK version
// import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

/**
 * Mecanum Drive Subsystem
 * 
 * This subsystem encapsulates all hardware and control logic for the mecanum drivetrain.
 * It integrates:
 * - 4 mecanum wheel motors
 * - RevIMU for field-centric driving
 * - goBILDA Pinpoint Odometry Computer for pose estimation
 * 
 * Features:
 * - Robot-centric and field-centric driving modes
 * - Odometry tracking with Pinpoint
 * - Heading management with IMU
 */
public class MecanumDriveSubsystem extends SubsystemBase {
    
    // ==================== Hardware ====================
    
    private final MotorEx frontLeft;
    private final MotorEx frontRight;
    private final MotorEx backLeft;
    private final MotorEx backRight;
    
    private final MecanumDrive drive;
    private final RevIMU imu;
    
    // Pinpoint Odometry Computer
    // NOTE: Uncomment and use the actual Pinpoint class when available
    // private GoBildaPinpointDriver pinpoint;
    private Object pinpoint; // Placeholder - replace with actual type
    
    // ==================== State ====================
    
    private boolean fieldCentricEnabled;
    private Pose2d currentPose;
    
    // ==================== Constructor ====================
    
    /**
     * Creates a new MecanumDriveSubsystem.
     * 
     * @param hardwareMap The hardware map from the OpMode
     */
    public MecanumDriveSubsystem(HardwareMap hardwareMap) {
        // Initialize motors
        frontLeft = new MotorEx(hardwareMap, DriveConstants.FRONT_LEFT_MOTOR);
        frontRight = new MotorEx(hardwareMap, DriveConstants.FRONT_RIGHT_MOTOR);
        backLeft = new MotorEx(hardwareMap, DriveConstants.BACK_LEFT_MOTOR);
        backRight = new MotorEx(hardwareMap, DriveConstants.BACK_RIGHT_MOTOR);
        
        // Configure motors
        configureMotors();
        
        // Create mecanum drive
        drive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);
        
        // Initialize IMU
        imu = new RevIMU(hardwareMap, OdometryConstants.IMU_NAME);
        imu.init();
        
        // Initialize Pinpoint
        initializePinpoint(hardwareMap);
        
        // Set initial state
        fieldCentricEnabled = DriveConstants.DEFAULT_FIELD_CENTRIC;
        currentPose = new Pose2d(
            OdometryConstants.INITIAL_POSE_X,
            OdometryConstants.INITIAL_POSE_Y,
            new Rotation2d(Math.toRadians(OdometryConstants.INITIAL_HEADING))
        );
    }
    
    // ==================== Configuration ====================
    
    /**
     * Configure motor properties (inversion, zero power behavior, etc.)
     */
    private void configureMotors() {
        // Set motor inversions
        frontLeft.setInverted(DriveConstants.INVERT_FRONT_LEFT);
        frontRight.setInverted(DriveConstants.INVERT_FRONT_RIGHT);
        backLeft.setInverted(DriveConstants.INVERT_BACK_LEFT);
        backRight.setInverted(DriveConstants.INVERT_BACK_RIGHT);
        
        // Set zero power behavior to brake for better control
        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        
        // Reset encoders
        frontLeft.resetEncoder();
        frontRight.resetEncoder();
        backLeft.resetEncoder();
        backRight.resetEncoder();
    }
    
    /**
     * Initialize the Pinpoint Odometry Computer.
     * 
     * @param hardwareMap The hardware map
     */
    private void initializePinpoint(HardwareMap hardwareMap) {
        if (!OdometryConstants.USE_PINPOINT) {
            return;
        }
        
        try {
            // TODO: Initialize Pinpoint when you have the correct library
            // Example initialization (adjust based on actual API):
            /*
            pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, OdometryConstants.PINPOINT_NAME);
            
            // Set pod offsets (in millimeters)
            pinpoint.setOffsets(
                OdometryConstants.X_OFFSET_MM,
                OdometryConstants.Y_OFFSET_MM
            );
            
            // Set encoder resolution
            pinpoint.setEncoderResolution(OdometryConstants.TICKS_PER_MM);
            
            // Set encoder directions (adjust based on your setup)
            pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
            );
            
            // Reset position
            pinpoint.resetPosAndIMU();
            */
            
        } catch (Exception e) {
            // If Pinpoint initialization fails, log it
            // The subsystem will still work without odometry
            System.err.println("Failed to initialize Pinpoint: " + e.getMessage());
            pinpoint = null;
        }
    }
    
    // ==================== Drive Methods ====================
    
    /**
     * Drive the robot using the configured mode (robot-centric or field-centric).
     * 
     * @param strafeSpeed Speed for lateral movement (-1.0 to 1.0, left negative)
     * @param forwardSpeed Speed for forward/backward movement (-1.0 to 1.0, backward negative)
     * @param turnSpeed Speed for rotation (-1.0 to 1.0, counterclockwise positive)
     */
    public void drive(double strafeSpeed, double forwardSpeed, double turnSpeed) {
        if (fieldCentricEnabled) {
            driveFieldCentric(strafeSpeed, forwardSpeed, turnSpeed);
        } else {
            driveRobotCentric(strafeSpeed, forwardSpeed, turnSpeed);
        }
    }
    
    /**
     * Drive the robot in robot-centric mode.
     * Movement is relative to the robot's orientation.
     * 
     * @param strafeSpeed Lateral speed
     * @param forwardSpeed Forward speed
     * @param turnSpeed Rotational speed
     */
    public void driveRobotCentric(double strafeSpeed, double forwardSpeed, double turnSpeed) {
        drive.driveRobotCentric(
            strafeSpeed,
            forwardSpeed,
            turnSpeed,
            DriveConstants.SQUARE_INPUTS
        );
    }
    
    /**
     * Drive the robot in field-centric mode.
     * Movement is relative to the field, not the robot's orientation.
     * 
     * @param strafeSpeed Lateral speed (field frame)
     * @param forwardSpeed Forward speed (field frame)
     * @param turnSpeed Rotational speed
     */
    public void driveFieldCentric(double strafeSpeed, double forwardSpeed, double turnSpeed) {
        drive.driveFieldCentric(
            strafeSpeed,
            forwardSpeed,
            turnSpeed,
            getHeading(), // Pass heading in degrees
            DriveConstants.SQUARE_INPUTS
        );
    }
    
    /**
     * Stop all motors.
     */
    public void stop() {
        drive.stop();
    }
    
    // ==================== Field-Centric Control ====================
    
    /**
     * Enable or disable field-centric driving mode.
     * 
     * @param enabled true for field-centric, false for robot-centric
     */
    public void setFieldCentric(boolean enabled) {
        this.fieldCentricEnabled = enabled;
    }
    
    /**
     * Toggle between field-centric and robot-centric modes.
     */
    public void toggleFieldCentric() {
        this.fieldCentricEnabled = !this.fieldCentricEnabled;
    }
    
    /**
     * Check if field-centric mode is enabled.
     * 
     * @return true if field-centric, false if robot-centric
     */
    public boolean isFieldCentric() {
        return fieldCentricEnabled;
    }
    
    // ==================== IMU Methods ====================
    
    /**
     * Get the current heading of the robot in degrees.
     * 
     * @return Heading in degrees (0-360, counterclockwise positive)
     */
    public double getHeading() {
        return imu.getHeading();
    }
    
    /**
     * Get the current heading as a Rotation2d object.
     * 
     * @return Rotation2d representing current heading
     */
    public Rotation2d getRotation2d() {
        return new Rotation2d(Math.toRadians(getHeading()));
    }
    
    /**
     * Reset the IMU heading to zero.
     * This sets the current direction as the new "forward" (0 degrees).
     */
    public void resetHeading() {
        imu.reset();
    }
    
    // ==================== Odometry Methods ====================
    
    /**
     * Get the current pose (position and heading) of the robot.
     * 
     * @return Current Pose2d
     */
    public Pose2d getPose() {
        return currentPose;
    }
    
    /**
     * Reset the odometry to a specific pose.
     * 
     * @param pose The new pose to set
     */
    public void resetOdometry(Pose2d pose) {
        this.currentPose = pose;
        
        // TODO: Reset Pinpoint when you have the correct library
        /*
        if (pinpoint != null) {
            pinpoint.setPosition(new Pose2D(
                DistanceUnit.INCH,
                pose.getX(),
                pose.getY(),
                AngleUnit.RADIANS,
                pose.getHeading()
            ));
        }
        */
    }
    
    /**
     * Reset odometry to origin (0, 0, 0).
     */
    public void resetOdometry() {
        resetOdometry(new Pose2d(0, 0, new Rotation2d(0)));
    }
    
    /**
     * Update odometry from the Pinpoint sensor.
     * This is called automatically in periodic().
     */
    private void updateOdometry() {
        if (pinpoint == null) {
            return;
        }
        
        // TODO: Update odometry when you have the correct Pinpoint library
        /*
        try {
            // Update Pinpoint (triggers a new reading)
            pinpoint.update();
            
            // Get the current position from Pinpoint
            Pose2D pinpointPose = pinpoint.getPosition();
            
            // Convert to FTCLib Pose2d (inches and radians)
            currentPose = new Pose2d(
                pinpointPose.getX(DistanceUnit.INCH),
                pinpointPose.getY(DistanceUnit.INCH),
                new Rotation2d(pinpointPose.getHeading(AngleUnit.RADIANS))
            );
            
        } catch (Exception e) {
            System.err.println("Pinpoint update failed: " + e.getMessage());
        }
        */
    }
    
    // ==================== Motor Information ====================
    
    /**
     * Get the average encoder position of all four motors.
     * Useful for simple distance calculations.
     * 
     * @return Average encoder ticks
     */
    public double getAverageEncoderPosition() {
        return (frontLeft.getCurrentPosition() + 
                frontRight.getCurrentPosition() + 
                backLeft.getCurrentPosition() + 
                backRight.getCurrentPosition()) / 4.0;
    }
    
    /**
     * Get encoder positions of all motors.
     * 
     * @return Array of [FL, FR, BL, BR] encoder positions
     */
    public double[] getEncoderPositions() {
        return new double[] {
            frontLeft.getCurrentPosition(),
            frontRight.getCurrentPosition(),
            backLeft.getCurrentPosition(),
            backRight.getCurrentPosition()
        };
    }
    
    /**
     * Get velocities of all motors.
     * 
     * @return Array of [FL, FR, BL, BR] velocities
     */
    public double[] getMotorVelocities() {
        return new double[] {
            frontLeft.getVelocity(),
            frontRight.getVelocity(),
            backLeft.getVelocity(),
            backRight.getVelocity()
        };
    }
    
    // ==================== Subsystem Periodic ====================
    
    /**
     * This method is called periodically by the CommandScheduler.
     * Use it to update odometry and other continuous tasks.
     */
    @Override
    public void periodic() {
        // Update odometry every loop
        updateOdometry();
        
        // Add any other periodic tasks here
    }
}
