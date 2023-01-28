// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeFrontSubsystem extends SubsystemBase {

  // NEO 550 * 4
  CANSparkMax motor1 = new CANSparkMax(IntakeConstants.motor1, MotorType.kBrushless);
  CANSparkMax motor2 = new CANSparkMax(IntakeConstants.motor2, MotorType.kBrushless);
  CANSparkMax motor3 = new CANSparkMax(IntakeConstants.motor3, MotorType.kBrushless);
  CANSparkMax motor4 = new CANSparkMax(IntakeConstants.motor4, MotorType.kBrushless);

  // Pneumatics
  Solenoid horizontal = new Solenoid(PneumaticsModuleType.CTREPCM, IntakeConstants.horizontalSolenoid);
  Solenoid vertical = new Solenoid(PneumaticsModuleType.CTREPCM, IntakeConstants.verticalSolenoid);

  /** Creates a new IntakeSubsystem. */
  public IntakeFrontSubsystem() {
    motor2.follow(motor1, false);
    motor4.follow(motor3, false);
    motor3.follow(motor1, true);
    intakeDown();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void intakeRun() {
    motor1.set(IntakeConstants.wheelSpeedRPM);
  }

  public void intakeUp() {
    vertical.set(true);
  }

  public void intakeDown() {
    vertical.set(false);
  }

  public void intakeOpen() {
    horizontal.set(true);
  }

  public void intakeClose() {
    horizontal.set(false);
  }

  public void horizontalStop() {
    horizontal.close();
  }

  public void verticalStop() {
    vertical.close();
  }

  public void wheelStop() {
    motor1.set(0);
  }
}