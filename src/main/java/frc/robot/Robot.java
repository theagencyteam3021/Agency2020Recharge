/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Robot extends TimedRobot {

 // private BallHandler ballHandler;

  private ArrayList<AgencySystem> activeSystems;

//  private Drive drive;

  private XboxController xbox;

 private Climber climber;

  // public String kEnable;
  // public String kDisable;

  @Override
  public void robotInit() {

    xbox = new XboxController(0);

    Boolean DEBUG = true;

    activeSystems = new ArrayList<AgencySystem>();

  //  ballHandler = new BallHandler("Ball Handler", DEBUG);

 //   drive = new Drive(RobotMap.lDriveFront, RobotMap.rDriveFront, RobotMap.lDriveBack, RobotMap.rDriveBack, "Drive",
 //       DEBUG);

    climber = new Climber(RobotMap.climber, "Climber", DEBUG);

 // activeSystems.add(ballHandler);

  //  activeSystems.add(drive);

    activeSystems.add(climber);

  }

  // autonomous

  @Override
  public void teleopInit() {
    activeSystems.forEach((n) -> n.teleopInit());
  }

  @Override
  public void teleopPeriodic() {

    // Drive
    // double XboxPosX = xbox.getX(Hand.kLeft); // was previsouly kRight
    // double XboxPosY = xbox.getTriggerAxis(Hand.kLeft) - xbox.getTriggerAxis(Hand.kRight);
    // drive.drive(-XboxPosY, XboxPosX);

    // // Ball Handler
    // if (xbox.getBButton()) {
    //   ballHandler.startLoad();
    // } else {
    //   ballHandler.stopLoad();
    // }

    // if (xbox.getAButton()) {
    //   ballHandler.shoot();
    // }

    // Climber
    if (xbox.getYButton()) {
      climber.raiseClimber();
    } else if (xbox.getXButton()) {
      climber.lowerClimber();
    } else {
      climber.stopClimber();
    }


    activeSystems.forEach((n) -> n.teleopPeriodic());

  }
}
