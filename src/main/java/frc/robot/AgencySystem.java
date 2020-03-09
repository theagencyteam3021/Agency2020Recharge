
package frc.robot;

public class AgencySystem{
    protected Boolean debug = false;
    protected String name;



    protected void console_debug(String string){
        if (!debug) return;
        System.out.println(this.getClass().getName() + " - " + string);
    }

 /*   protected void shuffleDebug(String name, boolean boo){
        Shuffleboard
    }*/

    // We do not have a  RobotInit() because these objects should only be created in RobotInit
    public void simulationInit(){};
    public void disabledInit(){};
    public void autonomousInit(){};
    public void teleopInit(){};
    public void testInit(){};
    public void robotPeriodic(){};
    public void simulationPeriodic(){};
    public void disabledPeriodic(){};
    public void autonomousPeriodic(){};
    public void teleopPeriodic(){};
    public void testPeriodic(){};
    

}