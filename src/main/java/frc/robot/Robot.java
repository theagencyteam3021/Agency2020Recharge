/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import java.util.ArrayList;

import edu.wpi.first.wpilibj.TimedRobot;


public class Robot extends TimedRobot {

  private Carousel carousel;

  private ArrayList<AgencySystem> activeSystems;

  // public String kEnable;
  // public String kDisable;

  @Override
  public void robotInit() {
    
    Boolean DEBUG = false;

    activeSystems = new ArrayList<AgencySystem>();


    carousel = new Carousel(17, DEBUG);
    activeSystems.add(carousel);

   

  }

  @Override
  public void teleopPeriodic() {
   

   
    activeSystems.forEach((n) -> n.teleopPeriodic());
  }
}
