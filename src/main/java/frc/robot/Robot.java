/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import java.util.ArrayList;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Robot extends TimedRobot {

  private BallHandler ballHandler;

  private Drive drive;

  private XboxController xbox, secondaryXbox;

  private Climber climber;

  private Autonomous auto;

  int mode;

  // public String kEnable;
  // public String kDisable;

  @Override
  public void robotInit() {

    xbox = new XboxController(0);
    secondaryXbox = new XboxController(1);

    Boolean DEBUG = false;

    ballHandler = new BallHandler("Ball Handler", DEBUG);

    drive = new Drive(RobotMap.lDriveFront, RobotMap.rDriveFront, RobotMap.lDriveBack, RobotMap.rDriveBack, "Drive",
        DEBUG);

    climber = new Climber(RobotMap.climber, "Climber", DEBUG);

    auto = new Autonomous();

  }

  // autonomous

  @Override
  public void teleopInit() {
    ballHandler.teleopInit();
    drive.teleopInit();
    climber.teleopInit();
  }

  @Override
  public void teleopPeriodic() {

    

    // Drive
    double XboxPosX = xbox.getX(Hand.kLeft); // was previsouly kRight
    double XboxPosY = xbox.getTriggerAxis(Hand.kRight) - xbox.getTriggerAxis(Hand.kLeft);

    if (xbox.getBumper(Hand.kLeft)) {
      drive.drive((-XboxPosY * 0.5), XboxPosX);
    } else {
      drive.drive(-XboxPosY, XboxPosX);
    }

    // // Ball Handler
    if (xbox.getBButton()) {
      ballHandler.startLoad();
    } else {
      ballHandler.stopLoad();
    }

    if (xbox.getAButton()) {
      ballHandler.shoot();
    }

    // Climber
    if (xbox.getYButton()) {
      climber.raiseClimber();
    } else if (xbox.getXButton()) {
      climber.lowerClimber();
    } else {
      climber.stopClimber();
    }

    /// Secondary Bail Controls
    if (secondaryXbox.getAButton()) {
      ballHandler.bailIntakeStage1();
    }
    if (secondaryXbox.getBButton()) {
      ballHandler.bailIntakeStage2();
    }
    if (secondaryXbox.getYButton()) {
      // ballHandler.bailCarousel();
    }

    ballHandler.teleopPeriodic();
    drive.teleopPeriodic();
    climber.teleopPeriodic();

  }

  public void autonomousInit() {
    ballHandler.autonomousInit();
    mode = 0;
  }
  
  public void autonomousPeriodic() {
    ballHandler.autonomousPeriodic();
    auto.autonomousPeriodic();

    ballHandler.startLoad();
    
    double[] autoController = auto.controller(mode);
    drive.drive(autoController[1],autoController[0]);
    if(autoController[2] == 1.) mode = 1;
    if(autoController[2] == 0.) mode = 0;
  }
}
