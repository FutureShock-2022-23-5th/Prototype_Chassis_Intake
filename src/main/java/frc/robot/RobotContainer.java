// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.server.PathPlannerServer;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.Chassis.LockPID;
import frc.robot.commands.Intake.IntakeCmd;
import frc.robot.commands.Intake.IntakeEnums.IntakeAction;
import frc.robot.commands.Intake.IntakeEnums.IntakeSide;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
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

  // Subsystems
  private final DriveSubsystem m_drive = new DriveSubsystem();
  private final IntakeSubsystem m_intake = new IntakeSubsystem();

  // Chassis Commands
  private final LockPID m_setPoint = new LockPID(m_drive);

  // Intake Commands
  private final IntakeCmd m_intakeFrontDownUp = new IntakeCmd(m_intake, IntakeSide.FRONT, IntakeAction.UPDOWN);
  private final IntakeCmd m_intakeFrontOpenClose = new IntakeCmd(m_intake, IntakeSide.FRONT, IntakeAction.OPENCLOSE);
  private final IntakeCmd m_intakeRearDownUp = new IntakeCmd(m_intake, IntakeSide.REAR, IntakeAction.UPDOWN);
  private final IntakeCmd m_intakeRearOpenClose = new IntakeCmd(m_intake, IntakeSide.REAR, IntakeAction.OPENCLOSE);

  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Drive
    // m_drive.setDefaultCommand(new RunCommand(() -> {
    //   m_drive.arcadeDrive(
    //     -driverJoystick.getRawAxis(OIConstants.leftStick_Y), driverJoystick.getRawAxis(OIConstants.rightStick_X));}
    //   , m_drive));

    m_drive.setDefaultCommand(new RunCommand(() -> {
      m_drive.arcadeDrive(
        -driverJoystick.getRawAxis(OIConstants.leftStick_Y) * 0.7, -driverJoystick.getRawAxis(OIConstants.rightStick_Y) * 0.7 );
    }, m_drive));

    // Configure the button bindings
    configureButtonBindings();

    PathPlannerServer.startServer(7130);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(driverJoystick, OIConstants.Btn_A)
      .onTrue(m_setPoint);

    new JoystickButton(driverJoystick, OIConstants.Btn_B)
      .onTrue( new RunCommand( () -> { m_drive.resetEncoders();}, m_drive));
      
    // new JoystickButton(driverJoystick, OIConstants.Btn_RB).toggleOnTrue(m_intakeFrontDownRun)
    //   .onFalse(m_intakeFrontStopUp);
    // new JoystickButton(driverJoystick, OIConstants.Btn_LB).toggleOnTrue(m_intakeRearDownRun)
    //   .onFalse(m_intakeRearStopUp);
    // new JoystickButton(driverJoystick, OIConstants.trigger_R).toggleOnTrue(m_intakeFrontOpen)
    //   .onFalse(m_intakeFrontClose);
    // new JoystickButton(driverJoystick, OIConstants.trigger_L).toggleOnTrue(m_intakeRearOpen)
    //   .onFalse(m_intakeRearClose);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_drive.followTrajectoryCommand("New Path", true);
  }
}
