package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

/**
 * Ultra-simple test: CommandOpMode without using commands
 * This tests if the issue is with CommandScheduler or with commands
 */
@TeleOp(name = "CommandOpMode Simple Test", group = "Testing")
public class CommandOpModeSimpleTest extends CommandOpMode {
    
    private MecanumDriveSubsystem driveSubsystem;
    
    @Override
    public void initialize() {
        driveSubsystem = new MecanumDriveSubsystem(hardwareMap);
        
        telemetry.addLine("Initialized");
        telemetry.addLine("This does NOT use commands");
        telemetry.addLine("Directly calls subsystem.drive()");
        telemetry.update();
    }
    
    @Override
    public void run() {
        // Don't call super.run() - bypass scheduler completely
        
        // Get stick values directly
        double strafe = -gamepad1.left_stick_x * 0.5;
        double forward = -gamepad1.left_stick_y * 0.5;
        double turn = -gamepad1.right_stick_x * 0.5;
        
        // Call subsystem directly (NO COMMANDS)
        driveSubsystem.drive(strafe, forward, turn);
        
        // Telemetry
        telemetry.clearAll();
        telemetry.addLine("=== DIRECT SUBSYSTEM TEST ===");
        telemetry.addData("Strafe", "%.3f", strafe);
        telemetry.addData("Forward", "%.3f", forward);
        telemetry.addData("Turn", "%.3f", turn);
        telemetry.addLine();
        telemetry.addData("Mode", driveSubsystem.isFieldCentric() ? "Field" : "Robot");
        telemetry.update();
    }
}
