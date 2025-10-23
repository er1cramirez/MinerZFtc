package org.firstinspires.ftc.teamcode.commands.drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

import java.util.function.DoubleSupplier;

/**
 * Ultra-simplified TeleopMecanumDrive for debugging
 * No speed modes, no input processing, just raw drive
 */
public class TeleopMecanumDriveSimple extends CommandBase {
    
    private final MecanumDriveSubsystem driveSubsystem;
    private final DoubleSupplier strafeSupplier;
    private final DoubleSupplier forwardSupplier;
    private final DoubleSupplier turnSupplier;
    
    private int executeCount = 0;
    
    public TeleopMecanumDriveSimple(
            MecanumDriveSubsystem driveSubsystem,
            DoubleSupplier strafeSupplier,
            DoubleSupplier forwardSupplier,
            DoubleSupplier turnSupplier) {
        
        this.driveSubsystem = driveSubsystem;
        this.strafeSupplier = strafeSupplier;
        this.forwardSupplier = forwardSupplier;
        this.turnSupplier = turnSupplier;
        
        addRequirements(driveSubsystem);
    }
    
    @Override
    public void initialize() {
        System.out.println("TeleopMecanumDriveSimple INITIALIZED");
    }
    
    @Override
    public void execute() {
        executeCount++;
        
        // Print every 50 loops to avoid spam
        if (executeCount % 50 == 0) {
            System.out.println("TeleopMecanumDriveSimple execute() called - count: " + executeCount);
        }
        
        // Get values directly, no processing
        double strafe = strafeSupplier.getAsDouble() * 0.5;
        double forward = forwardSupplier.getAsDouble() * 0.5;
        double turn = turnSupplier.getAsDouble() * 0.5;
        
        // Print values every 50 loops
        if (executeCount % 50 == 0) {
            System.out.println(String.format("Values: strafe=%.2f forward=%.2f turn=%.2f", 
                strafe, forward, turn));
        }
        
        // Drive
        driveSubsystem.drive(strafe, forward, turn);
    }
    
    @Override
    public void end(boolean interrupted) {
        System.out.println("TeleopMecanumDriveSimple ENDED - interrupted: " + interrupted);
        driveSubsystem.stop();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}
