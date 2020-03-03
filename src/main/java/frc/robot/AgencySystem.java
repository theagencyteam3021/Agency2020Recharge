
package frc.robot;


public class AgencySystem{
    protected Boolean debug = false;
    protected String name;


    protected void console_debug(String string){
        if (!debug) return;
        System.out.println(this.getClass().getName() + " - " + string);
    }

    // We do not have a  RobotInit() because these objects should only be created in RobotInit
    public void SimulationInit(){};
    public void DisabledInit(){};
    public void AutonomousInit(){};
    public void TeleopInit(){};
    public void TestInit(){};
    public void RobotPeriodic(){};
    public void SimulationPeriodic(){};
    public void DisabledPeriodic(){};
    public void AutonomousPeriodic(){};
    public void teleopPeriodic(){};
    public void TestPeriodic(){};

}