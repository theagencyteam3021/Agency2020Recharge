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


public class Robot extends TimedRobot {

  private BallHandler ballHandler;

  private ArrayList<AgencySystem> activeSystems;



  // public String kEnable;
  // public String kDisable;

  @Override
  public void robotInit() {
    
    
  
    Boolean DEBUG = true;

    activeSystems = new ArrayList<AgencySystem>();

    ballHandler = new BallHandler("Ball Handler", DEBUG);
    activeSystems.add(ballHandler);
    
  }

  //autonomous

  @Override
  public void teleopInit() {
    ballHandler.startLoad();
    activeSystems.forEach((n) -> n.teleopInit());
  }

  @Override
  public void teleopPeriodic() {

    activeSystems.forEach((n) -> n.teleopPeriodic());
  
  }
}
