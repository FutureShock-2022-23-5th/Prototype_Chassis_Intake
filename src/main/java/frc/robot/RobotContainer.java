// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.Chassis.PathFollowing;
import frc.robot.commands.Chassis.SetPoint;
import frc.robot.commands.Intake.Front.DownFront;
import frc.robot.commands.Intake.Front.RunFront;
import frc.robot.commands.Intake.Front.UpFront;
import frc.robot.commands.Intake.Rear.DownRear;
import frc.robot.commands.Intake.Rear.RunRear;
import frc.robot.commands.Intake.Rear.UpRear;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeFrontSubsystem;
import frc.robot.subsystems.IntakeRearSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // Joystick
  private final Joystick driverJoystick = new Joystick(0);
  private final Joystick operatorJoystick = new Joystick(1);

  // Subsystems
  private final DriveSubsystem m_drive = new DriveSubsystem();
  private final IntakeFrontSubsystem m_frontIntake = new IntakeFrontSubsystem();
  private final IntakeRearSubsystem m_rearIntake = new IntakeRearSubsystem();

  // Chassis Commands
  private final SetPoint m_setPoint = new SetPoint(m_drive);

  // Intake Commands
  private final RunFront m_intakeFrontRun = new RunFront(m_frontIntake);
  private final UpFront m_intakeFrontUp = new UpFront(m_frontIntake);
  private final DownFront m_intakeFrontDown = new DownFront(m_frontIntake);
  private final RunRear m_intakeRearRun = new RunRear(m_rearIntake);
  private final UpRear m_intakeRearUp = new UpRear(m_rearIntake);
  private final DownRear m_intakeRearDown = new DownRear(m_rearIntake);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Drive
    m_drive.setDefaultCommand(new RunCommand(() -> {
      m_drive.arcadeDrive(
        -driverJoystick.getRawAxis(OIConstants.leftStick_Y), driverJoystick.getRawAxis(OIConstants.rightStick_X));}
      , m_drive));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(driverJoystick, OIConstants.Btn_A).toggleOnTrue(m_setPoint);
    new JoystickButton(driverJoystick, OIConstants.Btn_RB).toggleOnTrue(m_intakeFrontRun);
    new JoystickButton(driverJoystick, OIConstants.Btn_LB).toggleOnTrue(m_intakeRearRun);
    new JoystickButton(driverJoystick, OIConstants.trigger_R)
      .toggleOnTrue(m_intakeFrontDown).whileFalse(m_intakeFrontUp);
    new JoystickButton(driverJoystick, OIConstants.trigger_L)
      .toggleOnTrue(m_intakeRearDown).whileFalse(m_intakeRearUp);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // // Create a voltage constraint to ensure we don't accelerate too fast
    // var autoVoltageConstraint =
    //     new DifferentialDriveVoltageConstraint(
    //         new SimpleMotorFeedforward(
    //             DriveConstants.ksVolts,
    //             DriveConstants.kvVoltSecondsPerMeter,
    //             DriveConstants.kaVoltSecondsSquaredPerMeter),
    //         DriveConstants.kDriveKinematics,
    //         10);

    // // Create config for trajectory
    // TrajectoryConfig config =
    //     new TrajectoryConfig(
    //             AutoConstants.kMaxSpeedMetersPerSecond,
    //             AutoConstants.kMaxAccelerationMetersPerSecondSquared)
    //         // Add kinematics to ensure max speed is actually obeyed
    //         .setKinematics(DriveConstants.kDriveKinematics)
    //         // Apply the voltage constraint
    //         .addConstraint(autoVoltageConstraint);

    // // An example trajectory to follow.  All units in meters.
    // PathPlannerTrajectory path1 = 
    //   PathPlanner.loadPath(
    //     "New Path", new PathConstraints(
    //       AutoConstants.kMaxSpeedMetersPerSecond, AutoConstants.kMaxAccelerationMetersPerSecondSquared));

    // RamseteCommand ramseteCommand =
    //     new RamseteCommand(
    //         path1,
    //         m_drive::getPose,
    //         new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
    //         new SimpleMotorFeedforward(
    //             DriveConstants.ksVolts,
    //             DriveConstants.kvVoltSecondsPerMeter,
    //             DriveConstants.kaVoltSecondsSquaredPerMeter),
    //         DriveConstants.kDriveKinematics,
    //         m_drive::getWheelSpeeds,
    //         new PIDController(DriveConstants.kPDriveVel, 0, 0),
    //         new PIDController(DriveConstants.kPDriveVel, 0, 0),
    //         // RamseteCommand passes volts to the callback
    //         m_drive::tankDriveVolts,
    //         m_drive);

    // // Reset odometry to the starting pose of the trajectory.
    // m_drive.resetOdometry(path1.getInitialPose());

    // // Run path following command, then stop at the end.
    // return ramseteCommand.andThen(() -> m_drive.tankDriveVolts(0, 0));

    return new SequentialCommandGroup(
      new PathFollowing(m_drive, "New Path"), 
      m_setPoint);
  }
}
