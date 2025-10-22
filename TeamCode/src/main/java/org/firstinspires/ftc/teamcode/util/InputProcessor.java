package org.firstinspires.ftc.teamcode.util;

/**
 * Utility class for processing raw joystick inputs.
 * Applies deadband and scaling to create smooth, responsive controls.
 */
public class InputProcessor {
    
    /**
     * Process a raw input value by applying deadband and scaling.
     * 
     * This method:
     * 1. Applies a deadband to eliminate joystick drift
     * 2. Rescales the remaining range to avoid sudden jumps
     * 3. Scales to the maximum speed value
     * 
     * @param rawInput Raw input value from joystick (-1.0 to 1.0)
     * @param deadband Deadband threshold (typically 0.05 to 0.15)
     * @param maxSpeed Maximum speed to scale to
     * @return Processed input value ready for motor control
     */
    public static double processInput(double rawInput, double deadband, double maxSpeed) {
        // Apply deadband and rescale
        double processed = applyDeadband(rawInput, deadband);
        
        // Scale to max speed
        return processed * maxSpeed;
    }
    
    /**
     * Apply deadband to an input value with smooth rescaling.
     * 
     * Values within the deadband are zeroed out.
     * Values outside are rescaled to eliminate the "jump" when exiting deadband.
     * 
     * Example with deadband = 0.1:
     * - Input 0.05 → Output 0.0 (within deadband)
     * - Input 0.1 → Output 0.0 (at deadband edge)
     * - Input 0.5 → Output ~0.44 (rescaled)
     * - Input 1.0 → Output 1.0 (maximum)
     * 
     * @param value Input value (-1.0 to 1.0)
     * @param deadband Deadband threshold (0.0 to 1.0)
     * @return Value with deadband applied and rescaled
     */
    public static double applyDeadband(double value, double deadband) {
        // If within deadband, return zero
        if (Math.abs(value) < deadband) {
            return 0.0;
        }
        
        // Rescale to eliminate jump at deadband edge
        // This creates a smooth transition from 0 to full range
        double sign = Math.signum(value);
        double magnitude = Math.abs(value);
        
        // Rescale: (value - deadband) / (1.0 - deadband)
        // This maps [deadband, 1.0] to [0.0, 1.0]
        double rescaled = (magnitude - deadband) / (1.0 - deadband);
        
        return sign * rescaled;
    }
    
    /**
     * Square an input while preserving its sign.
     * Useful for finer control at low speeds while maintaining full power at high speeds.
     * 
     * @param value Input value (-1.0 to 1.0)
     * @return Squared value with original sign
     */
    public static double squareInput(double value) {
        return Math.signum(value) * value * value;
    }
    
    /**
     * Process input with optional squaring for enhanced control.
     * 
     * @param rawInput Raw input value (-1.0 to 1.0)
     * @param deadband Deadband threshold
     * @param maxSpeed Maximum speed to scale to
     * @param square Whether to square the input for finer low-speed control
     * @return Processed input value
     */
    public static double processInput(double rawInput, double deadband, double maxSpeed, boolean square) {
        double processed = applyDeadband(rawInput, deadband);
        
        if (square) {
            processed = squareInput(processed);
        }
        
        return processed * maxSpeed;
    }
    
    /**
     * Clamp a value between a minimum and maximum.
     * 
     * @param value Value to clamp
     * @param min Minimum value
     * @param max Maximum value
     * @return Clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Normalize an angle to the range [-180, 180] degrees.
     * 
     * @param degrees Angle in degrees
     * @return Normalized angle in range [-180, 180]
     */
    public static double normalizeAngle(double degrees) {
        double angle = degrees % 360;
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }
        return angle;
    }
    
    // Prevent instantiation
    private InputProcessor() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
