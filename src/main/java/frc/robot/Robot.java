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

  private Carousel carousel;

  private XboxController xbox;

  // public String kEnable;
  // public String kDisable;

  @Override
  public void robotInit() {
    
    xbox = new XboxController(0);
  
    Boolean DEBUG = true;

    activeSystems = new ArrayList<AgencySystem>();

    ballHandler = new BallHandler("Ball Handler", DEBUG);
    //carousel = new Carousel(RobotMap.carousel, RobotMap.carouselBeam1, RobotMap.carouselBeam2,
       //                     RobotMap.carouselBeam3, "carousel", DEBUG);

    activeSystems.add(ballHandler);
    
  }

  //autonomous

  @Override
  public void teleopInit() {
    activeSystems.forEach((n) -> n.teleopInit());
  }

  @Override
  public void teleopPeriodic() {

    // if (xbox.getAButton()){
    //   carousel.requestForwardAdvance();
    // }
    // if (xbox.getXButton()){
    //   carousel.requestReverseAdvance();
    // }

    if (xbox.getBButton()){
      ballHandler.startLoad();
    }
    else{
      ballHandler.stopLoad();
    }

    if (xbox.getAButton()){
      ballHandler.shoot();
    }

    activeSystems.forEach((n) -> n.teleopPeriodic());
  
  }
}
