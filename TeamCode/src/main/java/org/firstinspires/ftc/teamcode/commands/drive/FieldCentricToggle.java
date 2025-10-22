package org.firstinspires.ftc.teamcode.commands.drive;

/**
 * Helper class to handle toggle button logic with debouncing.
 * 
 * This prevents multiple toggles from a single button press by detecting
 * only the rising edge (button press, not hold).
 * 
 * Usage:
 * <pre>
 * FieldCentricToggle toggle = new FieldCentricToggle();
 * 
 * // In your loop:
 * if (toggle.update(gamepad.start)) {
 *     // State changed
 *     boolean newState = toggle.get();
 *     subsystem.setFieldCentric(newState);
 * }
 * </pre>
 */
public class FieldCentricToggle {
    
    private boolean previousButtonState;
    private boolean currentValue;
    
    /**
     * Create a new toggle with initial state of false.
     */
    public FieldCentricToggle() {
        this(false);
    }
    
    /**
     * Create a new toggle with specified initial state.
     * 
     * @param initialState Initial toggle state
     */
    public FieldCentricToggle(boolean initialState) {
        this.previousButtonState = false;
        this.currentValue = initialState;
    }
    
    /**
     * Update the toggle state based on button input.
     * 
     * Detects rising edge (button press) and toggles the state.
     * Returns true if the state changed this update.
     * 
     * @param buttonPressed Current state of the button
     * @return true if state changed, false otherwise
     */
    public boolean update(boolean buttonPressed) {
        boolean stateChanged = false;
        
        // Detect rising edge (button just pressed, not held)
        if (buttonPressed && !previousButtonState) {
            // Toggle the value
            currentValue = !currentValue;
            stateChanged = true;
        }
        
        // Update previous state for next cycle
        previousButtonState = buttonPressed;
        
        return stateChanged;
    }
    
    /**
     * Get the current toggle state.
     * 
     * @return Current state (true or false)
     */
    public boolean get() {
        return currentValue;
    }
    
    /**
     * Set the toggle state directly.
     * 
     * @param value New state
     */
    public void set(boolean value) {
        this.currentValue = value;
    }
    
    /**
     * Reset the toggle to its initial state (false).
     */
    public void reset() {
        this.currentValue = false;
        this.previousButtonState = false;
    }
    
    /**
     * Reset the toggle to a specific state.
     * 
     * @param state State to reset to
     */
    public void reset(boolean state) {
        this.currentValue = state;
        this.previousButtonState = false;
    }
}
